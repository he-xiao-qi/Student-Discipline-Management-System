package com.example.sdms.controller;
import com.example.sdms.common.ResultVO;
import com.example.sdms.entity.User;
import com.example.sdms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResultVO<?> login(@RequestBody User user) {
        User dbUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (dbUser == null || !dbUser.getPassword().equals(user.getPassword())) {
            return ResultVO.error("用户名或密码错误");
        }
        if (!"active".equals(dbUser.getStatus())) {
            return ResultVO.error("账号未激活或已冻结");
        }
        // 可返回用户信息或token
        return ResultVO.success(dbUser);
    }
} 