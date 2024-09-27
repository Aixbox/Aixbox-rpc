
package com.aixbox.rpc.server.tcp;

import com.aixbox.rpc.RpcApplication;
import com.aixbox.rpc.exception.ErrorCode;
import com.aixbox.rpc.exception.RpcException;
import com.aixbox.rpc.model.RpcRequest;
import com.aixbox.rpc.model.RpcResponse;
import com.aixbox.rpc.model.ServiceMetaInfo;
import com.aixbox.rpc.protocol.ProtocolMessage;
import com.aixbox.rpc.protocol.ProtocolMessageDecoder;
import com.aixbox.rpc.protocol.ProtocolMessageEncoder;
import com.aixbox.rpc.protocol.ProtocolMessageTypeEnum;
import com.aixbox.rpc.registry.EtcdRegistry;
import com.aixbox.rpc.registry.LocalRegistry;
import com.aixbox.rpc.serializer.Serializer;
import com.aixbox.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Description: 请求处理
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 上午9:27
 */
public class TcpServerHandler implements Handler<NetSocket> {

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


    @Override
    public void handle(NetSocket netSocket) {

        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            //接受请求，解码
            ProtocolMessage<RpcRequest> protocolMessage;
            try {
                protocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RpcException(ErrorCode.DESERIALIZE_ERROR, "协议消息解码错误");
            }
            RpcRequest rpcRequest = protocolMessage.getBody();

            //设置有token的情况
            boolean isAuth = true;

            if (RpcApplication.getRpcConfig().isAuth()) {
                //请求的token
                String token = rpcRequest.getToken();
                ServiceMetaInfo serviceMetaInfo = EtcdRegistry.serviceCache.get("nodeInfo");
                if (!serviceMetaInfo.getToken().equals(token)) {
                    isAuth = false;
                }


            }

            //处理请求
            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            if (isAuth) {


                //获取要调用的服务实现类，通过反射调用
                try {
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
            } else {
                rpcResponse.setMessage("认证失败");
            }

            //发送响应，编码
            ProtocolMessage.Header header = protocolMessage.getHeader();
            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);
            try {
                Buffer encode = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                netSocket.write(encode);
            } catch (IOException e) {
                throw new RpcException(ErrorCode.SERIALIZE_ERROR, "协议消息编码错误");
            }


        });

        netSocket.handler(tcpBufferHandlerWrapper);


    }
}




;



















