package com.aixbox.rpcspringbootstarter.annotation;

import com.aixbox.rpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.aixbox.rpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.aixbox.rpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 启用Rpc注解
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午1:40
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动server
     * @return
     */
    boolean needServer() default true;

}
