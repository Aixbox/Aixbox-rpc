package com.aixbox.example.provider;

import com.aixbox.example.common.model.User;
import com.aixbox.example.common.service.UserService;
import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.bootstrap.ProviderBootstrap;
import com.aixbox.rpc.config.RegistryConfig;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.model.ServiceMetaInfo;
import com.aixbox.rpc.model.ServiceRegisterInfo;
import com.aixbox.rpc.registry.LocalRegistry;
import com.aixbox.rpc.registry.Registry;
import com.aixbox.rpc.registry.RegistryFactory;
import com.aixbox.rpc.server.HttpServer;
import com.aixbox.rpc.server.HttpServerHandler;
import com.aixbox.rpc.server.VertxHttpServer;
import com.aixbox.rpc.server.tcp.VertxTcpServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 服务提供者示例
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 下午2:44
 */
public class ProviderExample {

    public static void main(String[] args) throws Exception {
        UserServiceImpl userService = new UserServiceImpl();
        // 要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<?> serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), userService);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);

    }

}




















