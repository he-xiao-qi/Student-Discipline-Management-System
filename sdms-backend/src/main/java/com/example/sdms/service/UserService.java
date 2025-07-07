package com.example.sdms.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sdms.entity.User;

public interface UserService extends IService<User> {
    User getByUsername(String username);
} 