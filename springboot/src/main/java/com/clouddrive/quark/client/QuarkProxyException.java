package com.clouddrive.quark.client;

/** 上游夸克接口无法连接或响应无法处理时的异常。 */
public class QuarkProxyException extends RuntimeException {

    public QuarkProxyException(String message, Throwable cause) {
        super(message, cause);
    }
}
