package com.aixbox.rpc.fault.retry;

import com.aixbox.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * Description: 重试策略
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/15 下午10:34
 */
public interface RetryStrategy {

    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;

}
