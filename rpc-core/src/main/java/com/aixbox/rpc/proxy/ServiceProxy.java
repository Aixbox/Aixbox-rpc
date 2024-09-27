package com.aixbox.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.constant.RpcConstant;
import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;
import com.aixbox.rpc.fault.retry.RetryStrategy;
import com.aixbox.rpc.fault.retry.RetryStrategyFactory;
import com.aixbox.rpc.fault.tolerant.TolerantStrategy;
import com.aixbox.rpc.fault.tolerant.TolerantStrategyFactory;
import com.aixbox.rpc.loadbalancer.LoadBalancer;
import com.aixbox.rpc.loadbalancer.LoadBalancerFactory;
import com.aixbox.rpc.model.RpcReference;
import com.aixbox.rpc.model.RpcRequest;
import com.aixbox.rpc.model.RpcResponse;
import com.aixbox.rpc.model.ServiceMetaInfo;
import com.aixbox.rpc.protocol.*;
import com.aixbox.rpc.registry.Registry;
import com.aixbox.rpc.registry.RegistryFactory;
import com.aixbox.rpc.serializer.JdkSerializer;
import com.aixbox.rpc.serializer.Serializer;
import com.aixbox.rpc.serializer.SerializerFactory;
import com.aixbox.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Description: 服务代理（JDK 动态代理）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 下午1:19
 */
public class ServiceProxy implements InvocationHandler {

    private RpcReference rpcReference;

    public ServiceProxy(RpcReference rpcReference) {
        this.rpcReference = rpcReference;
    }

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        //构造请求
        String serviceName = method.getDeclaringClass().getSimpleName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        //从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RpcException(ErrorCode.SERVICE_CONSUMER_ERROR, "暂无服务地址");
        }

        //负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcReference.getLoadBalancer());
        //将调用方法名（请求路径）作为负载均衡参数
        HashMap<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        //发送TCP请求
        //使用重试策略
        RpcResponse rpcResponse;
        try {
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcReference.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("重试策略" + rpcReference.getRetryStrategy());
                return VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo);
            });
        } catch (Exception e) {
            //容错策略
            System.out.println("容错策略");
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcReference.getTolerantStrategy());
            HashMap<String, Object> tolerantParams = new HashMap<>();
            tolerantParams.put("class", rpcReference.getFackball());
            tolerantParams.put("rpcRequest", rpcRequest);
            rpcResponse = tolerantStrategy.doTolerant(tolerantParams, e);
        }

        return rpcResponse.getData();

        //    //发Http请求
        //    try(HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
        //            .body(bodyBytes)
        //            .execute()) {
        //        byte[] result = httpResponse.bodyBytes();
        //        //反序列化
        //        RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
        //        return rpcResponse.getData();
        //    }
        //
    }
}
