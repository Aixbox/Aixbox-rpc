package com.aixbox.rpc.fault.retry;

import com.aixbox.rpc.spi.SpiLoader;

/**
 * Description: 重试策略工厂（用于获取重试器对象）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/15 下午11:02
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试器
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }


}
