package com.aixbox.rpc.fault.tolerant;

import com.aixbox.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Description: 转移到其他服务节点 - 容错策略
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 上午10:50
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //todo
        return null;
    }
}
