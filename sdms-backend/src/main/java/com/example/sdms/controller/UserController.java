package com.example.sdms.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sdms.common.ResultVO;
import com.example.sdms.entity.User;
import com.example.sdms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResultVO<List<Map<String, Object>>> list(@RequestParam(required = false) String colleges,
                                                    @RequestParam(required = false) String roles,
                                                    @RequestParam(required = false) String status) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (StringUtils.hasText(colleges)) {
            for (String c : colleges.split(",")) {
                qw.like("colleges", c);
            }
        }
        if (StringUtils.hasText(roles)) {
            for (String r : roles.split(",")) {
                qw.apply("FIND_IN_SET({0}, roles)", r);
            }
        }
        if (StringUtils.hasText(status)) {
            qw.eq("status", status);
        }
        List<User> users = userService.list(qw);
        return ResultVO.success(users.stream().map(this::userToMap).collect(Collectors.toList()));
    }

    @GetMapping("/page")
    public ResultVO<IPage<Map<String, Object>>> page(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String colleges,
            @RequestParam(required = false) String roles,
            @RequestParam(required = false) String status) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.and(qw1 -> qw1
                .like("username", keyword)
                .or().like("name", keyword)
                .or().like("job_id", keyword)
                .or().like("colleges", keyword)
            );
        }
        if (StringUtils.hasText(colleges)) {
            for (String c : colleges.split(",")) {
                qw.like("colleges", c);
            }
        }
        if (StringUtils.hasText(roles)) {
            for (String r : roles.split(",")) {
                qw.apply("FIND_IN_SET({0}, roles)", r);
            }
        }
        if (StringUtils.hasText(status)) {
            qw.eq("status", status);
        }
        IPage<User> userPage = userService.page(new Page<>(page, size), qw);
        IPage<Map<String, Object>> mapPage = userPage.convert(this::userToMap);
        return ResultVO.success(mapPage);
    }

    @PostMapping("/save")
    public ResultVO<?> save(@RequestBody Map<String, Object> userMap) {
        User user = mapToUser(userMap);
        userService.saveOrUpdate(user);
        return ResultVO.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        userService.removeById(id);
        return ResultVO.success(null);
    }

    // 工具方法：User转Map，roles/colleges转数组
    private Map<String, Object> userToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("name", user.getName());
        map.put("jobId", user.getJobId());
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        map.put("roles", user.getRoles() != null ? Arrays.asList(user.getRoles().split(",")) : null);
        map.put("colleges", user.getColleges() != null ? Arrays.asList(user.getColleges().split(",")) : null);
        map.put("status", user.getStatus());
        map.put("addTime", user.getAddTime());
        return map;
    }
    // 工具方法：Map转User，roles/colleges转字符串
    private User mapToUser(Map<String, Object> map) {
        User user = new User();
        user.setId(map.get("id") != null ? Long.valueOf(map.get("id").toString()) : null);
        user.setName((String) map.get("name"));
        user.setJobId((String) map.get("jobId"));
        user.setUsername((String) map.get("username"));
        user.setPassword((String) map.get("password"));
        Object roles = map.get("roles");
        if (roles instanceof List) {
            user.setRoles(String.join(",", (List<String>) roles));
        } else if (roles instanceof String) {
            user.setRoles((String) roles);
        }
        Object colleges = map.get("colleges");
        if (colleges instanceof List) {
            user.setColleges(String.join(",", (List<String>) colleges));
        } else if (colleges instanceof String) {
            user.setColleges((String) colleges);
        }
        user.setStatus((String) map.get("status"));
        // addTime 只在新增时赋值
        if (user.getId() == null) {
            user.setAddTime(new java.util.Date());
        }
        return user;
    }
} 