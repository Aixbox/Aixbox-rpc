package com.aixbox.rpc.model;

import com.aixbox.rpc.constant.RpcConstant;
import lombok.*;

import java.io.Serializable;

/**
 * Description: Rpc请求
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/12 上午9:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcRequest implements Serializable {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 服务版本
     */
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表
     */
    private Object[] args;

    /**
     * token
     */
    private String token;



}














