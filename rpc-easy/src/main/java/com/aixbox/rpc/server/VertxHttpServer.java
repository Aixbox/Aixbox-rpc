package com.aixbox.rpc.server;

import io.vertx.core.Vertx;

/**
 * Description:
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/10 下午9:08
 */
public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        //创建Vert.x实例
        Vertx vertx = Vertx.vertx();

        //创建Http服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        //监听端口并处理请求
        httpServer.requestHandler(new HttpServerHandler());

        ////监听端口并处理请求
        //httpServer.requestHandler(request -> {
        //    //处理http请求
        //    System.out.println("Received request:" + request.method() + " " + request.uri());
        //
        //    //发送http响应
        //    request.response()
        //            .putHeader("content-type", "text/plain")
        //            .end("Hello from Vert.x Http server!");
        //});

        //启动http服务器并监听指定端口
        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.out.println("Failed to start server: " + result.cause());
            }
        });

    }
}



















