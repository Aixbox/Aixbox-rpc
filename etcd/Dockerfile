# 使用官方的 etcd 镜像
FROM quay.io/coreos/etcd:v3.5.6

# 创建 etcd 配置文件
COPY etcd.conf /etc/etcd/etcd.conf

# 设置环境变量
ENV ETCD_DATA_DIR=/etcd-data \
    ETCD_LISTEN_CLIENT_URLS=http://0.0.0.0:2379 \
    ETCD_LISTEN_PEER_URLS=http://0.0.0.0:2380 \
    ETCD_CLIENT_CERT_AUTH=true \
    ETCD_TRUSTED_CA_FILE=/etcd-data/ca.crt \
    ETCD_CERT_FILE=/etcd-data/server.crt \
    ETCD_KEY_FILE=/etcd-data/server.key \
    ETCD_AUTHENTICATION_MODE=enabled

# 暴露端口
EXPOSE 2379 2380

# 启动 etcd
ENTRYPOINT ["/usr/local/bin/etcd"]
CMD ["--config-file=/etc/etcd/etcd.conf"]
