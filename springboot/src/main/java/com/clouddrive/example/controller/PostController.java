package com.clouddrive.example.controller;

import com.clouddrive.common.ApiResponse;
import com.clouddrive.example.dto.PostDetail;
import com.clouddrive.example.dto.PostItem;
import com.clouddrive.example.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 帖子相关接口；后续帖子详情等接口也可以放在这个 Controller 中。 */
@RestController
@RequestMapping("/api/posts")
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
    @PostMapping("/detail")
    public ApiResponse<PostDetail> postsDetail(@RequestBody Map<String,String> req){
        String id = req.get("id");
        Long longId = Long.valueOf(id);
        System.out.println("id是"+id);
        return ApiResponse.success(postService.postDetail(longId));
    }

}
