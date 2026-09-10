package com.clouddrive.quark.controller;

import com.clouddrive.quark.dto.CreateQuarkShareRequest;
import com.clouddrive.quark.dto.SaveShareRequest;
import com.clouddrive.quark.dto.ShareIdRequest;
import com.clouddrive.quark.dto.SharePageTokenRequest;
import com.clouddrive.quark.service.QuarkShareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 分享、分享页和转存代理入口。 */
@RestController
@RequestMapping("/api/quark")
public class QuarkShareController {

    private final QuarkShareService service;

    public QuarkShareController(QuarkShareService service) {
        this.service = service;
    }

    @PostMapping("/shares")
    public ResponseEntity<String> create(
            @RequestBody CreateQuarkShareRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.create(request, cookie);
    }

    @PostMapping("/shares/url")
    public ResponseEntity<String> getShareUrl(
            @RequestBody ShareIdRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.getShareUrl(request, cookie);
    }

    @GetMapping("/shares")
    public ResponseEntity<String> listMine(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.listMine(page, size, cookie);
    }

    @DeleteMapping("/shares")
    public ResponseEntity<String> delete(
            @RequestBody ShareIdRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.delete(request, cookie);
    }

    @PostMapping("/share-page/token")
    public ResponseEntity<String> getShareToken(
            @RequestBody SharePageTokenRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.getShareToken(request, cookie);
    }

    @GetMapping("/share-page/files")
    public ResponseEntity<String> listShareFiles(
            @RequestParam String pwdId,
            @RequestParam String stoken,
            @RequestParam(defaultValue = "0") String pdirFid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.listShareFiles(pwdId, stoken, pdirFid, page, size, cookie);
    }

    @PostMapping("/share-page/save")
    public ResponseEntity<String> save(
            @RequestBody SaveShareRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.save(request, cookie);
    }
}
