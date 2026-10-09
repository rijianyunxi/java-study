package com.study.common;

/** 统一成功响应。T 表示 data 的具体 Java 类型。 */
public class ApiResponse<T> {

    private final int status;
    private final boolean result;
    // 响应提示信息，成功时默认为空字符串。
    private final String msg;
    private final T data;

    private ApiResponse(int status, boolean result, String msg, T data) {
        this.status = status;
        this.result = result;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, true, "", data);
    }
    public static <T> ApiResponse<T> error(T data) {
        return new ApiResponse<>(0, false, "", data);
    }

    /** 错误响应：status 保存 HTTP 状态码，msg 返回可读的错误信息。 */
    public static <T> ApiResponse<T> failure(int status, String msg) {
        return new ApiResponse<>(status, false, msg, null);
    }

    public int getStatus() {
        return status;
    }

    public boolean isResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
