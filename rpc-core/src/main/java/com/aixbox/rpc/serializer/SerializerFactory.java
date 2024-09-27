package com.aixbox.rpc.serializer;

import com.aixbox.rpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 序列化器工厂（用于获取序列化器对象）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 下午7:30
 */
public class SerializerFactory {

    /**
     * 序列化映射（用于实现单例）
     */
    static {
        SpiLoader.load(Serializer.class);
    }
    //private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<String, Serializer>() {{
    //    put(SerializerKeys.JSON, new JsonSerializer());
    //    put(SerializerKeys.JDK, new JdkSerializer());
    //    put(SerializerKeys.KRYO, new KryoSerializer());
    //    put(SerializerKeys.HESSIAN, new HessianSerializer());
    //}};

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }

}
