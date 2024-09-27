# Aixbox-RPC

一个简单的rpc框架，可通过注解在项目中快速使用



# 启动准备

需要提前安装[etcd](https://etcd.io/)

[docker安装etcd文档](./docs/etcd部署方式.md)



# 使用

1.在服务提供者和服务消费者中引入本项目的springboot starter依赖

```xml

```







# 

## 服务端

### 1.添加依赖

```xml
<dependency>
  <groupId>io.github.aixbox</groupId>
  <artifactId>rpc-spring-boot-starter</artifactId>
  <version>0.0.2</version>
</dependency>

```

### 2.启用 rpc 框架

在启动类添加注解 `<font style="color:#080808;background-color:#ffffff;">@EnableRpc</font>`



### 3.application.yml 添加配置

```yaml
rpc:
  name: rpc //服务名
  version: 2.0  //版本号
  serverHost: localhost //服务主机ip
  serverPort: 8088 //服务端口
  mock: false //是否开启模拟调用
  auth: true //是否开启服务接口鉴权
  serializer: json //序列化器 jdk | json | kryo | hessian
  loadBalancer: roundRobin //负载均衡器 roundRobin | random | consistentHash
  retryStrategy: fixedInterval //重试策略 fixedInterval | no
  tolerntStrategy: failFast //容错策略 failFast | failBack | FailOver | FailSafe
  registryConfig:
    registry: etcd //注册中心类型
    address: http://192.168.20.128:12379 //注册中心地址
    username: root //注册中心用户名
    password: 123456 //注册中心密码
    timeout:  10000 //注册中心超时时间
```



### 4.给方法添加注解

在提供服务的实现类上添加注解`<font style="color:#080808;background-color:#ffffff;">@RpcService</font>`



## 消费端

### 1.添加依赖

```yaml
        <dependency>
        <groupId>io.github.rpc</groupId>
        <artifactId>rpc-spring-boot-starter</artifactId>
        <version>0.0.1</version>
        </dependency>
```



### 2.在启动类添加注解

在启动类添加注解`<font style="color:#080808;background-color:rgba(255, 255, 255, 0);">@EnableRpc(needServer = false)</font>`<font style="color:#080808;background-color:rgba(255, 255, 255, 0);">，如果消费端没有任何服务需要提供，需要关闭服务器，默认为开启</font>

<font style="color:#080808;background-color:rgba(255, 255, 255, 0);"></font>

### <font style="color:#080808;background-color:rgba(255, 255, 255, 0);">3.application.yml 添加配置</font>

```yaml
rpc:
  name: rpc //服务名
  version: 2.0  //版本号
  mock: false //是否开启模拟调用
  auth: true //是否开启服务接口鉴权
  serializer: json //序列化器 jdk | json | kryo | hessian
  loadBalancer: roundRobin //负载均衡器 roundRobin | random | consistentHash
  retryStrategy: fixedInterval //重试策略 fixedInterval | no
  tolerntStrategy: failFast //容错策略 failFast | failBack | FailOver | FailSafe
  registryConfig:
    registry: etcd //注册中心类型
    address: http://192.168.20.128:12379 //注册中心地址
    username: root //注册中心用户名
    password: 123456 //注册中心密码
    timeout:  10000 //注册中心超时时间
```



### 4.给需要远程调用的属性添加注解

<font style="background-color:rgba(255, 255, 255, 0);">当容错策略为</font><font style="color:#080808;background-color:rgba(255, 255, 255, 0);">tolerantStrategy =</font><font style="background-color:rgba(255, 255, 255, 0);"> </font><font style="color:#080808;background-color:rgba(255, 255, 255, 0);">fallback 时，需要实现 此接口，并添加到 fallback 中</font>

```java
@RpcReference(tolerantStrategy = TolerantStrategyKeys.FAIL_BACK, fallback = ExampleServiceFallbackImpl.class)
private UserService userService;
```



```java
public class ExampleServiceFallbackImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("降级容错返回测试");
        return new User();
    }
}

```

