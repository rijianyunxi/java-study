package com.clouddrive.demo.controller;

import com.clouddrive.common.ApiResponse;
import com.clouddrive.demo.dto.CreateFolderRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 学习用接口：展示 Spring MVC 如何把 HTTP 请求中的不同位置的数据解析为 Java 参数。
 * 此类仅回显解析结果，因此不额外创建 Service；真实业务逻辑应交给对应模块的 Service。
 */
@RestController
@RequestMapping("/api/demo")
public class ParameterDemoController {

    /**
     * 示例请求：GET /api/demo/query?name=小明&page=2&keyword=照片
     * name 为必传参数；page 不传时默认是 1；keyword 不传时为 null。
     */
    @GetMapping("/query")
    public ApiResponse<Map<String, Object>> query(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword) {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
        data.put("page", page);
        data.put("keyword", keyword);
        return ApiResponse.success(data);
    }

    /**
     * 示例请求：GET /api/demo/files/123
     * {id} 是路径占位符；Spring 把 URL 中的 123 转换成 Long 类型的 fileId。
     */
    @GetMapping("/files/{id}")
    public ApiResponse<Map<String, Object>> fileDetail(
            @PathVariable("id") Long fileId) {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("fileId", fileId);
        return ApiResponse.success(data);
    }

    /**
     * 示例请求：POST /api/demo/folders，且请求体为 JSON。
     * @RequestBody 让 Jackson 把 JSON 转成 CreateFolderRequest 对象。
     */
    @PostMapping("/folders")
    public ApiResponse<CreateFolderRequest> createFolder(
            @RequestBody CreateFolderRequest request) {

        // 仅演示“接收并解析”；没有保存到数据库，也没有真正创建文件夹。
        return ApiResponse.success(request);
    }

    /**
     * 示例请求需携带请求头：X-Client-Name: web-client。
     * 不携带该头时，clientName 使用默认值 anonymous。
     */
    @GetMapping("/header")
    public ApiResponse<Map<String, Object>> header(
            @RequestHeader(value = "X-Client-Name", defaultValue = "anonymous") String clientName) {

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("clientName", clientName);
        return ApiResponse.success(data);
    }
}
