package com.aixbox.rpc.registry;

import com.aixbox.rpc.spi.SpiLoader;

/**
 * Description: 注册中心工厂（用于获取注册中心对象）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/13 上午9:26
 */
public class RegistryFactory {

    static{
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }

}
