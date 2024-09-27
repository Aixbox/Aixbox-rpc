package com.aixbox.rpc.bootstrap;

import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.config.RegistryConfig;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;
import com.aixbox.rpc.model.ServiceMetaInfo;
import com.aixbox.rpc.model.ServiceRegisterInfo;
import com.aixbox.rpc.registry.LocalRegistry;
import com.aixbox.rpc.registry.Registry;
import com.aixbox.rpc.registry.RegistryFactory;
import com.aixbox.rpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * Description: 服务提供者初始化
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午1:02
 */
public class ProviderBootstrap {

    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList) {
        //Rpc框架初始化（配置和注册中心）
        RpcApplication.init();
        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //注册服务
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfoList) {
            String serviceName = serviceRegisterInfo.getServiceName();
            //本地注册
            LocalRegistry.register(serviceName, serviceRegisterInfo.getInstanceObject());

            //注册服务到注册中心
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RpcException(ErrorCode.SERVICE_PROVIDER_ERROR, serviceName + " 服务注册失败", e);
            }
        }
        // 启动服务器
        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getServerPort());

    }

}























