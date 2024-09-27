package com.aixbox.rpc.model;

import com.aixbox.rpc.fault.retry.RetryStrategyKeys;
import com.aixbox.rpc.fault.tolerant.TolerantStrategyKeys;
import com.aixbox.rpc.loadbalancer.LoadBalancerKeys;
import lombok.Data;

/**
 * Description: Rpc动态代理参数
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 下午4:42
 */
@Data
public class RpcReference {

    /**
     * 负载均衡器
     * @return
     */
    private String loadBalancer;

    /**
     * 重试策略
     * @return
     */
    private String retryStrategy;

    /**
     * 容错策略
     * @return
     */
    private String tolerantStrategy;

    /**
     * 降级响应类
     */
    private Class<?> fackball;

    /**
     * 模拟调用
     * @return
     */
    private boolean mock;




}
