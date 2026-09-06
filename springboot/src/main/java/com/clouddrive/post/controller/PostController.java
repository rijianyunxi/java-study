package com.clouddrive.post.controller;

import com.clouddrive.common.ApiResponse;
import com.clouddrive.post.dto.PostItem;
import com.clouddrive.post.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 帖子相关接口；后续帖子详情等接口也可以放在这个 Controller 中。 */
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/list")
    public ApiResponse<List<PostItem>> list() {
        // List 序列化为 JSON 数组，每个 PostItem 序列化为 {id, title} 对象。
        return ApiResponse.success(postService.listPosts());
    }
}
