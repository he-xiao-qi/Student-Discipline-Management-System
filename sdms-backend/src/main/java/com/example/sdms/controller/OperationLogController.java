package com.example.sdms.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sdms.common.ResultVO;
import com.example.sdms.entity.OperationLog;
import com.example.sdms.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import java.util.List;

@RestController
@RequestMapping("/api/operation-log")
public class OperationLogController {
    @Autowired
    private OperationLogService service;

    @GetMapping("/list")
    public ResultVO<List<OperationLog>> list(@RequestParam(required = false) String college) {
        return ResultVO.success(service.selectWithUserName());
    }

    @GetMapping("/page")
    public ResultVO<IPage<OperationLog>> page(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String college) {
        QueryWrapper<OperationLog> qw = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.like("action", keyword);
        }
        if (StringUtils.hasText(college)) {
            qw.eq("college", college);
        }
        return ResultVO.success(service.page(new Page<>(page, size), qw));
    }

    @GetMapping("/{id}")
    public OperationLog get(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping("/save")
    public void save(@RequestBody OperationLog log) {
        service.save(log);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.removeById(id);
    }
} 