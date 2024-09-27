package com.aixbox.rpc.serializer;

import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;

import java.io.*;

/**
 * Description: JDK序列化器
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 上午8:40
 */
public class JdkSerializer implements Serializer{
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        try {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RpcException(ErrorCode.DESERIALIZE_ERROR, e);
        } finally {
            objectInputStream.close();
        }
    }
}
