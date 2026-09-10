package com.clouddrive.quark.controller;

import com.clouddrive.quark.dto.QrCodeData;
import com.clouddrive.quark.service.QuarkAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 扫码登录相关代理。 */
@RestController
@RequestMapping("/api/quark/auth")
public class QuarkAuthController {

    private final QuarkAuthService service;

    public QuarkAuthController(QuarkAuthService service) {
        this.service = service;
    }

    @GetMapping("/qrcode")
    public QrCodeData qrcode() {
        return service.createQrCode();
    }

    @GetMapping("/qrcode/status")
    public ResponseEntity<String> status(@RequestParam String token) {
        return service.pollQrCode(token);
    }

    @GetMapping("/session")
    public ResponseEntity<String> session(@RequestParam String ticket) {
        ResponseEntity<String> upstream = service.exchangeTicket(ticket);
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(upstream.getStatusCode());
        if (upstream.getHeaders().getContentType() != null) {
            builder.contentType(upstream.getHeaders().getContentType());
        }
        // 浏览器同源访问时可读取这个头，手动复制 Cookie 到页面的 Cookie 输入框。
        List<String> setCookies = upstream.getHeaders().get("Set-Cookie");
        if (setCookies != null) {
            for (String setCookie : setCookies) {
                builder.header("X-Quark-Set-Cookie", setCookie);
            }
        }
        return builder.body(upstream.getBody());
    }
}
