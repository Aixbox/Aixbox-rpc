package com.aixbox.rpc.exception;

import lombok.Getter;

/**
 * Description: 错误类型枚举
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/17 下午1:14
 */
@Getter
public enum ErrorCode {
    SERIALIZE_ERROR(1101, "序列化失败"),
    DESERIALIZE_ERROR(1102, "反序列化失败"),
    SPI_ERROR(1103, "SPI加载失败"),
    SERVICE_PROVIDER_ERROR(2001, "服务提供错误"),
    SERVICE_CONSUMER_ERROR(2002, "服务消费错误"),
    REGISTER_ERROR(3001, "注册中心错误"),
    PRC_SYSTEM_ERROR(4001, "rpc系统错误"),
    PROTOCOL_ERROR(5001, "协议错误");
    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
