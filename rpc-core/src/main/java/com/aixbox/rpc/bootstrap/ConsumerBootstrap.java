package com.aixbox.rpc.bootstrap;

import com.aixbox.rpc.RpcApplication;

/**
 * Description: 服务消费者启动类（初始化）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午1:21
 */
public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init() {
        //Rpc框架初始化（配置和注册中心）
        RpcApplication.init();
    }

}
