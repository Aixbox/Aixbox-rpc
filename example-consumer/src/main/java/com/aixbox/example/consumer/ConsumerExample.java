package com.aixbox.example.consumer;

import com.aixbox.example.common.model.User;
import com.aixbox.example.common.service.UserService;
import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.bootstrap.ConsumerBootstrap;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.model.RpcReference;
import com.aixbox.rpc.proxy.ServiceProxyFactory;

/**
 * Description: 服务消费者代码
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 下午2:40
 */
public class ConsumerExample {

    public static void main(String[] args) {
        //RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        //System.out.println(rpc);
        ConsumerBootstrap.init();

        RpcReference rpcReference = new RpcReference();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        rpcReference.setLoadBalancer(rpcConfig.getLoadBalancer());
        rpcReference.setRetryStrategy(rpcConfig.getRetryStrategy());
        rpcReference.setTolerantStrategy(rpcConfig.getTolerantStrategy());
        rpcReference.setMock(rpcConfig.isMock());

        UserService userService;
        userService = ServiceProxyFactory.getProxy(UserService.class, rpcReference);
        User user = new User();
        user.setName("aixbox222");
        User newUser = userService.getUser(user);
        User newUser1 = userService.getUser(user);
        User newUser2 = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
        User number = userService.getNumber();
        System.out.println(number);
    }

}
