package com.aixbox.rpc.config;

import com.aixbox.rpc.fault.retry.RetryStrategyKeys;
import com.aixbox.rpc.fault.tolerant.TolerantStrategyKeys;
import com.aixbox.rpc.loadbalancer.LoadBalancerKeys;
import com.aixbox.rpc.serializer.Serializer;
import com.aixbox.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * Description: Rpc框架配置
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 下午2:05
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8083;

    /**
     * 模拟调用
     */
    private boolean mock = false;

    /**
     * 服务鉴权配置
     */
    private boolean auth = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.FIXED_INTERVAL;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();


}
















