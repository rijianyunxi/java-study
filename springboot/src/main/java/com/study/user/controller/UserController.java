package com.study.user.controller;

import com.study.common.ApiResponse;
import com.study.user.dto.CreateUserRequest;
import com.study.user.dto.UpdateUserRequest;
import com.study.user.dto.UpdateUserStatusRequest;
import com.study.user.entity.User;
import com.study.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/** 独立的 User 表演示接口，不改动原有帖子接口。 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<User>> list() {
        return ApiResponse.success(userService.listUsers());
    }

    @GetMapping("/search")
    public ApiResponse<List<User>> findByName(@RequestParam("name") String name) {
        return ApiResponse.success(userService.findByName(name));
    }

    /** GET /api/users/1：详情。 */
    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable("id") Long id) {
        return ApiResponse.success(userService.getUserById(id));
    }

    /** POST /api/users：新增，JSON 包含 id、name、age、email。 */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<User> create(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.success(userService.createUser(request));
    }

    /** PUT /api/users/1：完整修改基本资料，不修改 ID 和状态。 */
    @PutMapping("/{id}")
    public ApiResponse<User> update(@PathVariable("id") Long id,
                                    @Valid @RequestBody UpdateUserRequest request) {
        return ApiResponse.success(userService.updateUser(id, request));
    }

    /** PATCH /api/users/1/status：只修改状态。 */
    @PatchMapping("/{id}/status")
    public ApiResponse<User> updateStatus(@PathVariable("id") Long id,
                                         @Valid @RequestBody UpdateUserStatusRequest request) {
        return ApiResponse.success(userService.updateStatus(id, request.getStatus()));
    }
}
