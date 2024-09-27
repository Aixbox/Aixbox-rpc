package com.aixbox.example.provider;

import com.aixbox.example.common.service.UserService;
import com.aixbox.rpc.registry.LocalRegistry;
import com.aixbox.rpc.server.VertxHttpServer;

/**
 * Description: 简易服务提供者示例
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/10 下午8:43
 */
public class EasyProviderExample {



    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        //注册服务
        LocalRegistry.register(UserService.class.getName(), userService);

        //提供服务
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(8080);
    }

}
