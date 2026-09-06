package com.clouddrive.welcome.controller;

import com.clouddrive.common.ApiResponse;
import com.clouddrive.welcome.dto.WelcomeData;
import com.clouddrive.welcome.service.WelcomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 接口层：接收 HTTP 请求，调用业务层，返回 Java 对象。 */
@RestController
@RequestMapping("/api")
public class WelcomeController {

    private final WelcomeService welcomeService;

    // 构造器注入：WelcomeService 由 Spring 创建并传入，不需要手动 new。
    public WelcomeController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @GetMapping("/hello")
    public ApiResponse<WelcomeData> welcome() {
        return ApiResponse.success(welcomeService.getWelcomeData());
    }

    @GetMapping("/hello2")
    public String welcome2() {
        return "1";
    }
}