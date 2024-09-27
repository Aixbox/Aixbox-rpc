## Aixbox-RPC

一个简单的rpc框架，可通过注解在项目中快速使用



## 启动准备

需要提前安装[etcd](https://etcd.io/) 

版本：



## 使用

1.在服务提供者和服务消费者中引入本项目的springboot starter依赖

```xml
        <dependency>
            <groupId>com.aixbox</groupId>
            <artifactId>rpc-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```



2.添加rpc配置

```yml
rpc:
  name: rpc 
  version: 2.0
  serverHost: localhost
  serverPort: 8088
  mock: false
  serializer: json
  loadBalancer: roundRobin
  retryStrategy: fixedInterval
  registryConfig:
    registry: etcd
    address: http://192.168.20.128:2379
    username: 
    password:
    timeout: 
```



3.Springboot启动类添加注解`@EnableRpc`



4.服务提供者类添加注解`@RpcService`



5.@EnableRpc(needServer = false)  todo