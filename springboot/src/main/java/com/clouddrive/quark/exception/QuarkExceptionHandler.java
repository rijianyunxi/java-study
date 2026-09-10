package com.clouddrive.quark.exception;

import com.clouddrive.quark.client.QuarkProxyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/** 把网络错误转换成前端容易理解的 JSON，而不是返回 Spring 默认 HTML 错误页。 */
@RestControllerAdvice(basePackages = "com.clouddrive.quark")
public class QuarkExceptionHandler {

    @ExceptionHandler(QuarkProxyException.class)
    public ResponseEntity<Map<String, Object>> handleProxyError(QuarkProxyException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", 502);
        body.put("result", false);
        body.put("msg", ex.getMessage());
        body.put("data", null);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body);
    }
}
