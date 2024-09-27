package com.aixbox.rpc.registry;

import com.aixbox.rpc.model.ServiceMetaInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;

import java.time.Duration;
import java.util.List;

/**
 * Description: 服务发现缓存
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/17 下午2:28
 */
public class RegistryServiceMultiCache {

    private static final Duration EXPIRE_TIME = Duration.ofHours(1);
    private static final Long MAX_MUM_SIZE = 10000L;

    /**
     * 服务缓存
     */
    public static final Cache<String, List<ServiceMetaInfo>> serviceCache = Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE_TIME)
            .maximumSize(MAX_MUM_SIZE)
            .build();

    /**
     * 写缓存
     *
     * @param serviceKey 服务键名
     * @param newServiceCache 更新后的缓存列表
     * @return
     */
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache.put(serviceKey, newServiceCache);
    }

    /**
     * 读缓存
     *
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> readCache(String serviceKey) {
        return this.serviceCache.getIfPresent(serviceKey);
    }

    /**
     * 清空缓存
     */
    void clearCache(String serviceKey) {
        this.serviceCache.invalidate(serviceKey);
    }

}
