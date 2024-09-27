package com.aixbox.rpc.serializer;

import java.io.IOException;

/**
 * Description: 序列化器接口
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 上午12:00
 */
public interface Serializer {

    /**
     * 序列化
     * @param obj
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;

}
