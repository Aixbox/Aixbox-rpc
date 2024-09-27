package com.aixbox.rpc.exception;

/**
 * Description: RPC 消费端异常
 *
 * @author <a href="https://github.com/Aixbox">Aixbox</a>
 * Date 2024/9/17 下午1:10
 */
public class RpcException extends RuntimeException{

    /**
     * 错误码
     */
    protected Integer code;


    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RpcException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.code = errorCode.getCode();
    }

    public RpcException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public RpcException(ErrorCode errorCode, String message, Exception e) {
        super(message, e);
        this.code = errorCode.getCode();
    }

}
