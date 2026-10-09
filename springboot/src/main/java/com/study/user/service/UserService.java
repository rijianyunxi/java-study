package com.study.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.study.mapper.UserMapper;
import com.study.user.dto.CreateUserRequest;
import com.study.user.dto.UpdateUserRequest;
import com.study.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** 用户示例业务：读取、插入、修改资料及独立修改状态。 */
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /** 按 ID 排序，确保示例列表顺序稳定。 */
    public List<User> listUsers() {
        return userMapper.selectList(new LambdaQueryWrapper<User>().orderByAsc(User::getId));
    }

    /** 详情查询：不存在时返回 HTTP 404，而不是成功响应中的 null。 */
    public User getUserById(Long id) {
        requirePositiveId(id);
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "用户不存在：" + id);
        }
        return user;
    }

    public List<User> findByName(String name) {
        return userMapper.selectList(new LambdaQueryWrapper<User>()
                .eq(User::getName, name).orderByAsc(User::getId));
    }

    /** 状态由服务端设置为启用；主键冲突由异常处理器返回 HTTP 409。 */
    @Transactional
    public User createUser(CreateUserRequest request) {
        User user = new User();
        user.setId(request.getId());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());
        user.setStatus(1);
        userMapper.insert(user);
        return user;
    }

    /** 只更新 name / age / email，避免读出整个实体后覆盖并发修改的状态。 */
    @Transactional
    public User updateUser(Long id, UpdateUserRequest request) {
        requirePositiveId(id);
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .eq(User::getId, id)
                .set(User::getName, request.getName())
                .set(User::getAge, request.getAge())
                .set(User::getEmail, request.getEmail()));
        // 同值更新可能报告 0 行；查询存在性而不是仅根据受影响行数判断。
        return getUserById(id);
    }

    /** 只生成 UPDATE user SET status = ? WHERE id = ?，不修改其他列。 */
    @Transactional
    public User updateStatus(Long id, Integer status) {
        requirePositiveId(id);
        if (status == null || (status != 0 && status != 1)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "状态只能是 0 或 1");
        }
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .eq(User::getId, id).set(User::getStatus, status));
        return getUserById(id);
    }

    private void requirePositiveId(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用户 ID 必须大于 0");
        }
    }
}
