package com.aixbox.rpc.fault.tolerant;

import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;
import com.aixbox.rpc.model.RpcRequest;
import com.aixbox.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Description: 降级到其他服务 - 容错策略
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/16 上午10:45
 */
@Slf4j
public class FailBackTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        RpcRequest rpcRequest = (RpcRequest) context.get("rpcRequest");
        Class<?> aClass = (Class<?>) context.get("class");
        if (aClass.equals(Void.class)){
            throw new RpcException(ErrorCode.SERVICE_CONSUMER_ERROR, "未设置fallback");
        }
        //获取降级的服务并调用
        //构造响应结果对象
        RpcResponse rpcResponse = new RpcResponse();
        try {
            Method method = aClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            Object result = method.invoke(aClass.newInstance(), rpcRequest.getArgs());
            //封装返回结果
            rpcResponse.setData(result);
            rpcResponse.setDataType(method.getReturnType());
            rpcResponse.setMessage("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            rpcResponse.setMessage(ex.getMessage());
            rpcResponse.setException(ex);
        }

        return rpcResponse;
    }
}
