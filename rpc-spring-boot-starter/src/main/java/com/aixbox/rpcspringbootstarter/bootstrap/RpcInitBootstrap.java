package com.aixbox.rpcspringbootstarter.bootstrap;

import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.config.RpcConfig;
import com.aixbox.rpc.server.tcp.VertxTcpServer;
import com.aixbox.rpcspringbootstarter.annotation.EnableRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Description: Rpc框架启动
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午1:58
 */
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    private static final Logger log = LoggerFactory.getLogger(RpcInitBootstrap.class);

    /**
     * Spring初始化时执行，初始化Rpc框架
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取EnableRpc注解的属性值
        boolean needServer = (boolean)importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("needServer");

        //Rpc框架初始化（配置和注册中心）
        RpcApplication.init();

        //全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //启动服务器
        if (needServer) {
            VertxTcpServer vertxTcpServer = new VertxTcpServer();
            vertxTcpServer.doStart(rpcConfig.getServerPort());
        } else {
            log.info("不启动server");
        }


    }
}
