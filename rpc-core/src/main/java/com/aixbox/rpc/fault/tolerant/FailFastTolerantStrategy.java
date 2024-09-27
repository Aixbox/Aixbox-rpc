package com.aixbox.rpc.fault.tolerant;

import com.aixbox.rpc.model.RpcResponse;

import java.util.Map;

/**
 * Description: 快速失败 - 容错策略（立刻通知外层调用方）
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 上午10:38
 */
public class FailFastTolerantStrategy implements TolerantStrategy{

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务错误", e);
    }
}
