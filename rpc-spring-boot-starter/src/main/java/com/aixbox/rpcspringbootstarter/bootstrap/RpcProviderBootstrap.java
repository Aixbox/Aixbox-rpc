package com.aixbox.rpcspringbootstarter.bootstrap;

import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.config.RegistryConfig;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;
import com.aixbox.rpc.model.ServiceMetaInfo;
import com.aixbox.rpc.registry.LocalRegistry;
import com.aixbox.rpc.registry.Registry;
import com.aixbox.rpc.registry.RegistryFactory;
import com.aixbox.rpcspringbootstarter.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Description: Rpc服务提供者启动
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午2:07
 */
public class RpcProviderBootstrap implements BeanPostProcessor {

    /**
     * Bean初始化后执行，注册服务
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            //需要注册服务
            //1.获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            //默认值处理
            if (interfaceClass == void.class) {
                //获取实现的第一个接口
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getSimpleName();
            String serviceVersion = rpcService.serviceVersion();
            //2.注册服务
            //本地注册
            LocalRegistry.register(serviceName, bean);

            //全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            //注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RpcException(ErrorCode.SERVICE_PROVIDER_ERROR, serviceName + " 服务注册失败", e);
            }
        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
