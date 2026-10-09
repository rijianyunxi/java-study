package com.clouddrive.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.clouddrive.mapper.UserMapper;
import com.clouddrive.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/** 用户示例业务：演示 MyBatis-Plus 的查询方法。 */
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /** 查询 user 表中的所有用户。 */
    public List<User> listUsers() {
        return userMapper.selectList(null);
    }

    /** 按主键 id 查询用户；不存在时返回 null。 */
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /** 按姓名精确查询，演示 MyBatis-Plus 条件构造器。 */
    public List<User> findByName(String name) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("name", name);
        return userMapper.selectList(query);
    }
}
