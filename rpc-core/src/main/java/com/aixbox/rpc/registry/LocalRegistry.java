package com.aixbox.rpc.registry;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: 本地注册中心
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/10 下午10:04
 */
public class LocalRegistry {

    /**
     * 注册信息存储
     */
    private static final Map<String, Object> map = new ConcurrentHashMap<>();

    /**
     * 注册服务
     * @param serviceName
     * @param instanceObject
     */
    public static void register(String serviceName, Object instanceObject) {
        map.put(serviceName, instanceObject);
    }

    /**
     * 获取服务
     * @param serviceName
     * @return
     */
    public static Object get(String serviceName) {
        return map.get(serviceName);
    }

    /**
     * 删除服务
     * @param serviceName
     */
    public static void remove(String serviceName) {
        map.remove(serviceName);
    }

}



















