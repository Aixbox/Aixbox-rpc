package com.aixbox.rpc.proxy;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Description: Mock服务代理（JDK动态代理）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 下午4:34
 */

public class MockServiceProxy implements InvocationHandler {

    private static final Logger log = LoggerFactory.getLogger(MockServiceProxy.class);

    private final Faker faker = new Faker();

    /**
     * 调用代理
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 生成指定类型的默认值对象
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type) {
        // 基本类型生成随机假数据
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return faker.bool().bool();
            } else if (type == byte.class) {
                return (byte) faker.number().numberBetween(Byte.MIN_VALUE, Byte.MAX_VALUE);
            } else if (type == char.class) {
                return (char) faker.number().numberBetween(65, 90); // 生成随机大写字母
            } else if (type == short.class) {
                return (short) faker.number().numberBetween(Short.MIN_VALUE, Short.MAX_VALUE);
            } else if (type == int.class) {
                return faker.number().numberBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else if (type == long.class) {
                return faker.number().numberBetween(Long.MIN_VALUE, Long.MAX_VALUE);
            } else if (type == float.class) {
                return (float) faker.number().randomDouble(2, 0, 1000);
            } else if (type == double.class) {
                return faker.number().randomDouble(2, 0, 1000);
            }
        }

        // 包装类型和其他常见类型
        if (type == Boolean.class) {
            return faker.bool().bool();
        } else if (type == Byte.class) {
            return (byte) faker.number().numberBetween(Byte.MIN_VALUE, Byte.MAX_VALUE);
        } else if (type == Character.class) {
            return (char) faker.number().numberBetween(65, 90); // 随机生成大写字母
        } else if (type == Short.class) {
            return (short) faker.number().numberBetween(0, Short.MAX_VALUE);
        } else if (type == Integer.class) {
            return faker.number().numberBetween(0, Integer.MAX_VALUE);
        } else if (type == Long.class) {
            return faker.number().numberBetween(0L, Long.MAX_VALUE);
        } else if (type == Float.class) {
            return (float) faker.number().randomDouble(2, 0, 1000);
        } else if (type == Double.class) {
            return faker.number().randomDouble(2, 0, 1000);
        } else if (type == String.class) {
            return faker.lorem().word();
        } else if (type == java.util.Date.class) {
            return faker.date().birthday();
        } else if (type == java.time.LocalDate.class) {
            return faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        }

        // 自定义类处理
        if (!type.isPrimitive() && !type.isEnum()) {
            return createCustomObject(type);
        }

        // 其他类型返回 null
        return null;
    }

    /**
     * 生成自定义类的假数据
     * @param type
     * @return
     */
    private Object createCustomObject(Class<?> type) {
        try {
            // 创建对象实例
            Object instance = type.getDeclaredConstructor().newInstance();

            // 遍历所有字段并设置假数据
            Field[] fields = type.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 允许访问私有字段

                // 递归生成字段的默认值
                Object fieldValue = getDefaultObject(field.getType());

                // 设置字段值
                field.set(instance, fieldValue);
            }

            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

















