package com.example.sdms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sdms.entity.User;
import com.example.sdms.mapper.UserMapper;
import com.example.sdms.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    @Transactional
    public boolean saveOrUpdate(User user) {
        if (user.getId() != null) {
            // 编辑
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                // 密码为空时不更新密码
                return userMapper.updateUserWithoutPassword(user) > 0;
            }
        }
        // 新增或有密码时正常保存
        return super.saveOrUpdate(user);
    }
} 