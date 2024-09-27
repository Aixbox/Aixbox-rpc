package com.aixbox.rpc.server.tcp;

import com.aixbox.rpc.server.HttpServer;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.parsetools.RecordParser;

/**
 * Description: tcp服务器
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/15 下午1:35
 */
public class VertxTcpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建TCP服务器
        NetServer server = vertx.createNetServer();

        //处理请求
        server.connectHandler(new TcpServerHandler());

        //server.connectHandler(socket -> {
        //    // 构造 parser
        //    RecordParser parser = RecordParser.newFixed(8);
        //    parser.setOutput(new Handler<Buffer>() {
        //        // 初始化
        //        int size = -1;
        //        // 一次完整的读取（头 + 体）
        //        Buffer resultBuffer = Buffer.buffer();
        //
        //        @Override
        //        public void handle(Buffer buffer) {
        //            if (-1 == size) {
        //                // 读取消息体长度
        //                size = buffer.getInt(4);
        //                parser.fixedSizeMode(size);
        //                // 写入头信息到结果
        //                resultBuffer.appendBuffer(buffer);
        //            } else {
        //                // 写入体信息到结果
        //                resultBuffer.appendBuffer(buffer);
        //                System.out.println(resultBuffer.toString());
        //                // 重置一轮
        //                parser.fixedSizeMode(8);
        //                size = -1;
        //                resultBuffer = Buffer.buffer();
        //            }
        //        }
        //    });
        //
        //    socket.handler(parser);
        //});


        //server.connectHandler(socket -> {
        //    //处理连接
        //    socket.handler(buffer -> {
        //        //处理接收到的字节数组
        //        byte[] requestData = buffer.getBytes();
        //        //在这里进行自定义的字节数组处理逻辑，比如解析请求，调用服务，构造响应等
        //        byte[] responseData = handleRequest(requestData);
        //        //发送响应
        //        socket.write(Buffer.buffer(responseData));
        //    });
        //});

        //启动TCp服务器并监听指定端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP server started on port" + port);
            } else {
                System.out.println("Failed to start TCP server: " + result.cause());
            }

        });
    }

    private byte[] handleRequest(byte[] requestData) {
        //在这里编写处理请求的逻辑， 根据requestData构造响应数据并返回
        //这里只是一个示例，实际逻辑需要根据具体的业务需求实现
        return "Hello, client!".getBytes();
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8888);
    }
 }





















