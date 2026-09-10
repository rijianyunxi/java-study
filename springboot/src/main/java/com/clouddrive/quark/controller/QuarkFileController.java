package com.clouddrive.quark.controller;

import com.clouddrive.quark.dto.CreateQuarkFolderRequest;
import com.clouddrive.quark.dto.DeleteQuarkFilesRequest;
import com.clouddrive.quark.dto.DownloadQuarkFilesRequest;
import com.clouddrive.quark.dto.MoveQuarkFilesRequest;
import com.clouddrive.quark.dto.RenameQuarkFileRequest;
import com.clouddrive.quark.service.QuarkFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 夸克文件 API 的本地代理入口。响应体保持上游 JSON，方便前端直接使用。 */
@RestController
@RequestMapping("/api/quark")
public class QuarkFileController {

    private final QuarkFileService service;

    public QuarkFileController(QuarkFileService service) {
        this.service = service;
    }

    @GetMapping("/files")
    public ResponseEntity<String> list(
            @RequestParam(defaultValue = "0") String pdirFid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String sort,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.list(pdirFid, page, size, sort, cookie);
    }

    @GetMapping("/files/{fid}")
    public ResponseEntity<String> detail(
            @PathVariable String fid,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.detail(fid, cookie);
    }

    @PostMapping("/files/folders")
    public ResponseEntity<String> createFolder(
            @RequestBody CreateQuarkFolderRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.createFolder(request, cookie);
    }

    @PostMapping("/files/delete")
    public ResponseEntity<String> delete(
            @RequestBody DeleteQuarkFilesRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.delete(request, cookie);
    }

    @PostMapping("/files/rename")
    public ResponseEntity<String> rename(
            @RequestBody RenameQuarkFileRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.rename(request, cookie);
    }

    @GetMapping("/files/search")
    public ResponseEntity<String> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String sort,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.search(q, page, size, sort, cookie);
    }

    @GetMapping("/files/tree")
    public ResponseEntity<String> tree(
            @RequestParam(defaultValue = "0") String pdirFid,
            @RequestParam(defaultValue = "5") int maxDepth,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.tree(pdirFid, maxDepth, cookie);
    }

    @PostMapping("/files/move")
    public ResponseEntity<String> move(
            @RequestBody MoveQuarkFilesRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.move(request, cookie);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<String> task(
            @PathVariable String taskId,
            @RequestParam(defaultValue = "0") int retryIndex,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.task(taskId, retryIndex, cookie);
    }

    @PostMapping("/files/download")
    public ResponseEntity<String> download(
            @RequestBody DownloadQuarkFilesRequest request,
            @RequestHeader(value = "X-Quark-Cookie", required = false) String cookie) {
        return service.download(request, cookie);
    }
}
