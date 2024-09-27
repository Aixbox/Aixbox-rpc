package com.aixbox.rpc.registry;

import com.aixbox.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * Description:
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/14 下午9:57
 */
public class RegistryServiceCache {

    ServiceMetaInfo serviceCache;

    void writeCache(ServiceMetaInfo newServiceCache) {
        this.serviceCache = newServiceCache;
    }

    ServiceMetaInfo readCache() {
        return this.serviceCache;
    }

    void clearCache() {
        this.serviceCache = null;
    }

}
