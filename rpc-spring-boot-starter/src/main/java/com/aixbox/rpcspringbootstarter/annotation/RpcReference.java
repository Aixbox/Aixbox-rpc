package com.aixbox.rpcspringbootstarter.annotation;

import com.aixbox.rpc.constant.RpcConstant;
import com.aixbox.rpc.fault.retry.RetryStrategyKeys;
import com.aixbox.rpc.fault.tolerant.TolerantStrategyKeys;
import com.aixbox.rpc.loadbalancer.LoadBalancerKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 服务消费者注解（用于注入服务）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午1:47
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcReference {

    /**
     * 服务接口类
     * @return
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     * @return
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     * @return
     */
    String loadBalancer() default LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     * @return
     */
    String retryStrategy() default RetryStrategyKeys.FIXED_INTERVAL;

    /**
     * 容错策略
     * @return
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    /**
     * 降级响应类
     * @return
     */
    Class<?> fallback() default Void.class;

    /**
     * 模拟调用
     * @return
     */
    boolean mock() default false;

}





























