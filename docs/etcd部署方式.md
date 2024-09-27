## 1.创建docker-compose.yml
找一个目录创建 docker-compose.yml 文件

```plain
vim docker-compose.yml 
```

```yaml
version: '3'

networks:
  etcd-cluster:
    external: true

services:
  etcd-node1:
    image: quay.io/coreos/etcd:v3.5.5
    container_name: etcd-node1
    ports:
      - "12379:2379"
      - "12380:2380"
    restart: always
    volumes:
      - ./etcd-node1:/data/app/etcd
    command: >
      etcd --name etcd-node1 
           --data-dir /data/app/etcd/ 
           --advertise-client-urls http://172.25.0.10:2379 
           --initial-advertise-peer-urls http://172.25.0.10:2380 
           --listen-client-urls http://0.0.0.0:2379 
           --listen-peer-urls http://0.0.0.0:2380 
           --initial-cluster-token etcd-cluster 
           --initial-cluster "etcd-node1=http://172.25.0.10:2380" 
           --initial-cluster-state new
    networks:
      etcd-cluster:
        ipv4_address: 172.25.0.10

```



## 2.创建 docker 网络
使用下面的命令创建 docker 容器使用的网络

```yaml
docker network create --driver bridge --subnet 172.25.0.0/16 --gateway 172.25.0.1 etcd-cluster
```





## 3.启动 etcd
执行命令

```yaml
docker-compose up -d
```



## 4.验证 etcd
```yaml
//进入etcd容器的命令行
docker exec -it etcd-node1 bash
//验证容器
etcdctl member list -w table
```

![](https://cdn.nlark.com/yuque/0/2024/webp/38508127/1727278167059-4a0571bf-5517-407b-99a7-31640c2bcc67.webp)

```yaml
etcdctl --endpoints=172.25.0.10:2379 endpoint status -w table
```

![](https://cdn.nlark.com/yuque/0/2024/webp/38508127/1727278195459-1162adfc-be62-4337-ad71-ec74f696e68c.webp)



## 5.读写数据
```yaml
etcdctl put /foo bar
etcdctl get /foo
```



## 6.开启 auth 身份认证
### 1)创建 etcd 角色
```yaml
etcdctl --endpoints=172.25.0.10:2379 role add root
```



### 2）创建 etcd 用户
```yaml
etcdctl --endpoints=172.25.0.10:2379 user add root:123456
```



### 3)将角色赋予用户
```yaml
etcdctl --endpoints=172.25.0.10:2379 user grant-role root root //第一个是用户，第二个是角色
```



### 4）开启 etcd 用户认证
```yaml
etcdctl --endpoints=172.25.0.10:2379 auth enable
```



### 6)测试用户认证
没有指定用户名和密码时，会报错，如下：

```yaml
[root@localhost ~]# etcdctl --endpoints=172.25.0.10:2379 get /mykey
{"level":"warn","ts":"2020-09-28T11:29:04.668+0800","caller":"clientv3/retry_interceptor.go:62","msg":"retrying of 
unary invoker failed","target":"endpoint://client-7f830105-4bac-4ec5-8b5e-1e97181639a5/192.168.56.200:2379","attempt":0,"error":"rpc error: code = InvalidArgument desc = etcdserver: user name is empty"}
Error: etcdserver: user name is empty
```

指定正确的用户名和密码，再次重试：

```yaml
[root@localhost ~]# etcdctl --endpoints=${endpoints} --user=admin:123456 put /mykey 'myvalue'
OK
[root@localhost ~]# etcdctl --endpoints=${endpoints} --user=admin:123456 get /mykey
/mykey
myvalue
```





## 7.在 windows 使用 etcdKeeper 访问
### 1）下载程序
下载 etcdKeeper 的 windeows 程序[https://github.com/evildecay/etcdkeeper/releases](https://github.com/evildecay/etcdkeeper/releases) 

![](https://cdn.nlark.com/yuque/0/2024/png/38508127/1727278795208-43b5e07c-114a-49f8-b1d3-5919fe1ef5eb.png)



### 2）启动程序
在 [issue#57](https://github.com/evildecay/etcdkeeper/issues/57)和 README.md 文件中得知，开启 auth 后需要 添加 `-auth`启动程序

打开 cmd 执行命令

```yaml
etcdkeeper.exe -auth
```

之后首次打开页面会有登录提示

![](https://cdn.nlark.com/yuque/0/2024/png/38508127/1727279068375-fd489b41-6635-450b-9ac7-c633bddea6f1.png)



填写宿主机地址和账号密码后就能访问了

![](https://cdn.nlark.com/yuque/0/2024/png/38508127/1727279122715-e36ff441-9dbb-4093-b8eb-3fe0dc7f6b9d.png)



### 3）其他
有时启动后页面一直加载，可用将 cmd 关闭并且网页保持打开，再次点击网页就可以进行修改了



## 8.参考
[docker 部署 etcd 集群及使用](https://developer.aliyun.com/article/1385959)

[etcd集群开启用户身份认证](https://developer.aliyun.com/article/1164188)

























