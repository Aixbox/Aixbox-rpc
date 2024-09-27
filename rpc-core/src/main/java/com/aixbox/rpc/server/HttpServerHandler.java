package com.aixbox.rpc.server;

import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.model.RpcRequest;
import com.aixbox.rpc.model.RpcResponse;
import com.aixbox.rpc.registry.LocalRegistry;
import com.aixbox.rpc.serializer.JdkSerializer;
import com.aixbox.rpc.serializer.Serializer;
import com.aixbox.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Description: Http请求处理
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 上午9:27
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //记录日志
        System.out.println("Received request: " + request.method() + " " + request.uri());

        //异步处理http请求
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            //如果请求为null,直接返回
            if (rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                //获取要调用服务的实现类，通过反射调用
                Object instanceObject = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = instanceObject.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                Object result = method.invoke(instanceObject, rpcRequest.getArgs());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            //序列化
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }


}
