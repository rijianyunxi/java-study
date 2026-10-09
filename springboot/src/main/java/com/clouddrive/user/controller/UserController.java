package com.clouddrive.user.controller;

import com.clouddrive.common.ApiResponse;
import com.clouddrive.user.entity.User;
import com.clouddrive.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 独立的 User 表演示接口，不改动原有帖子接口。 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /** GET /api/users：查询全部用户。 */
    @GetMapping
    public ApiResponse<List<User>> list() {
        return ApiResponse.success(userService.listUsers());
    }

    /** GET /api/users/search?name=Jack：按姓名精确查询。 */
    @GetMapping("/search")
    public ApiResponse<List<User>> findByName(@RequestParam String name) {
        return ApiResponse.success(userService.findByName(name));
    }

    /** GET /api/users/1：按主键查询单个用户。 */
    @GetMapping("/{id}")
    public ApiResponse<User> getById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ApiResponse.error(null);
        }
        return ApiResponse.success(user);
    }
}
