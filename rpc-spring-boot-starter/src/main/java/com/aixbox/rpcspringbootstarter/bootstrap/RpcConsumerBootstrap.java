package com.aixbox.rpcspringbootstarter.bootstrap;

import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;
import com.aixbox.rpc.proxy.ServiceProxyFactory;
import com.aixbox.rpcspringbootstarter.annotation.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * Description: Rpc 服务消费者启动
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午2:19
 */
public class RpcConsumerBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行，注入服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 遍历对象的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);

                com.aixbox.rpc.model.RpcReference rpcReferenceParams = new com.aixbox.rpc.model.RpcReference();
                rpcReferenceParams.setLoadBalancer(rpcReference.loadBalancer());
                rpcReferenceParams.setRetryStrategy(rpcReference.retryStrategy());
                rpcReferenceParams.setTolerantStrategy(rpcReference.tolerantStrategy());
                rpcReferenceParams.setMock(rpcReference.mock());
                rpcReferenceParams.setFackball(rpcReference.fallback());


                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass, rpcReferenceParams);
                try {
                    field.set(bean, proxyObject);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RpcException(ErrorCode.SERVICE_CONSUMER_ERROR, "为字段注入代理对象失败", e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
