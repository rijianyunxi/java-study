package com.clouddrive.quark.controller;

import com.clouddrive.quark.dto.QrCodeData;
import com.clouddrive.quark.service.QuarkAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/** 扫码登录相关代理。 */
@RestController
@RequestMapping("/api/quark/auth")
public class QuarkAuthController {

    static final String QUARK_COOKIE_HEADER = "X-Quark-Cookie";

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

        String cookie = extractCookie(upstream.getHeaders().get(HttpHeaders.SET_COOKIE));
        if (StringUtils.hasText(cookie)) {
            // 仅返回浏览器请求所需的 name=value 片段，避免前端错误拆分包含 Expires 日期的 Set-Cookie。
            builder.header(QUARK_COOKIE_HEADER, cookie);
        }
        return builder.body(upstream.getBody());
    }

    private static String extractCookie(List<String> setCookies) {
        if (setCookies == null || setCookies.isEmpty()) {
            return "";
        }

        List<String> cookiePairs = new ArrayList<>();
        for (String setCookie : setCookies) {
            if (!StringUtils.hasText(setCookie)) {
                continue;
            }
            int separator = setCookie.indexOf(';');
            String cookiePair = (separator >= 0 ? setCookie.substring(0, separator) : setCookie).trim();
            if (StringUtils.hasText(cookiePair)) {
                cookiePairs.add(cookiePair);
            }
        }
        return String.join("; ", cookiePairs);
    }
}
