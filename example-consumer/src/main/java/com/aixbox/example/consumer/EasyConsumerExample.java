package com.aixbox.example.consumer;

import com.aixbox.example.common.model.User;
import com.aixbox.example.common.service.UserService;
import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.model.RpcReference;
import com.aixbox.rpc.proxy.ServiceProxyFactory;

/**
 * Description: 简易服务消费者示例
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/10 下午8:46
 */
public class EasyConsumerExample {

    public static void main(String[] args) {

        RpcReference rpcReference = new RpcReference();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        rpcReference.setLoadBalancer(rpcConfig.getLoadBalancer());
        rpcReference.setRetryStrategy(rpcConfig.getRetryStrategy());
        rpcReference.setTolerantStrategy(rpcConfig.getTolerantStrategy());
        rpcReference.setMock(rpcConfig.isMock());

        //todo 需要获取 UserService 的实现类对象
        UserService userService = ServiceProxyFactory.getProxy(UserService.class, rpcReference);
        User user = new User();
        user.setName("aixbox11111111");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }

}







