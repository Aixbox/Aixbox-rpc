package com.aixbox.rpc.registry;

import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.config.RegistryConfig;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;
import com.aixbox.rpc.model.ServiceMetaInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.etcd.jetcd.*;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import io.vertx.core.impl.ConcurrentHashSet;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * etcd 注册中心实现
 */

public class EtcdRegistry implements Registry{

    private Client client;

    private KV kvClient;

    /**
     * 注册的节点键集合
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 获取的服务节点缓存
     */
    private final RegistryServiceMultiCache registryServiceMultiCache = new RegistryServiceMultiCache();

    /**
     * 服务节点缓存
     */
    public static final ConcurrentMap<String, ServiceMetaInfo> serviceCache = new ConcurrentHashMap<>();


    /**
     * 监听的服务节点key的集合
     */
    private final Set<String> watchingKeySet = new ConcurrentHashSet<>();

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        if (registryConfig.getUsername() != null) {
            client = Client.builder()
                    .endpoints(registryConfig.getAddress())
                    .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                    .user(ByteSequence.from(registryConfig.getUsername(), StandardCharsets.UTF_8))
                    .password(ByteSequence.from(registryConfig.getPassword(), StandardCharsets.UTF_8))
                    .build();
        } else {
            client = Client.builder()
                    .endpoints(registryConfig.getAddress())
                    .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                    .build();
        }
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建Lease和KV客户端
        Lease leaseClient = client.getLeaseClient();

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //设置uuid生成的随机token
        if (rpcConfig.isAuth()) {
            UUID uuid = UUID.randomUUID();
            serviceMetaInfo.setToken(uuid.toString());
        }


        long leaseId;

        try {
            //创建一个30秒的租约
            leaseId = leaseClient.grant(30).get(5, TimeUnit.SECONDS).getID();
        } catch (Exception e) {
            throw new RpcException(ErrorCode.REGISTER_ERROR, "创建租约失败", e);
        }

        //设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);

        //保存
        serviceCache.put("nodeInfo", serviceMetaInfo);

        //将键值对与租约关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();

        localRegisterNodeKeySet.add(registerKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKey, StandardCharsets.UTF_8));

        localRegisterNodeKeySet.remove(registerKey);
    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        //List<ServiceMetaInfo> cacheServiceMetaInfoList = registryServiceMultiCache.readCache(serviceKey);
        //if (cacheServiceMetaInfoList != null && !cacheServiceMetaInfoList.isEmpty()) {
        //    return cacheServiceMetaInfoList;
        //}

        //前缀搜索，结尾加'/'
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";

        try {
            //前缀搜索
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.get(
                    ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                    getOption)
                    .get()
                    .getKvs();
            //解析服务信息
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream()
                    .map(keyValue -> {
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        //监听服务节点
                        watch(key, serviceKey);

                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    })
                    .collect(Collectors.toList());

            //服务节点写入缓存
            //registryServiceMultiCache.writeCache(serviceKey ,serviceMetaInfoList);

            return serviceMetaInfoList;
        } catch (Exception e) {
            throw new RpcException(ErrorCode.REGISTER_ERROR, "获取服务列表失败", e);
        }
    }




    @Override
    public void destroy() {
        System.out.println("当前节点下线");

        for (String key : localRegisterNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(key, StandardCharsets.UTF_8)).get();
            } catch (Exception e) {
                throw new RpcException(ErrorCode.REGISTER_ERROR, key + "节点下线失败");
            }
        }

        //释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }


    @Override
    public void heartBeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                for (String key : localRegisterNodeKeySet) {
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(key, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        KeyValue keyValue = keyValues.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo serviceMetaInfo = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        register(serviceMetaInfo);
                    } catch (Exception e) {
                        throw new RpcException(ErrorCode.REGISTER_ERROR, key + "续签失败" + e);
                    }
                }
            }
        });

        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey, String serviceKey) {
        Watch watchClient = client.getWatchClient();

        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch) {
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8), response -> {
                for (WatchEvent event : response.getEvents()) {
                    switch (event.getEventType()) {
                        case DELETE:
                            registryServiceMultiCache.clearCache(serviceKey);
                        case PUT:
                        default:
                            break;

                    }
                }
            });
        }

    }



    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // create client using endpoints
        Client client = Client.builder().endpoints("http://localhost:2379")
                .build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        // put the key-value
        kvClient.put(key, value).get();

        // get the CompletableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        // get the value from CompletableFuture
        GetResponse response = getFuture.get();

        // delete the key
        kvClient.delete(key).get();
    }
}