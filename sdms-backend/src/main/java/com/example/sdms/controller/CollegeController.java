package com.example.sdms.controller;

import com.example.sdms.common.ResultVO;
import com.example.sdms.entity.College;
import com.example.sdms.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/college")
public class CollegeController {
    @Autowired
    private CollegeService collegeService;

    @GetMapping("/list")
    public ResultVO<List<College>> list() {
        return ResultVO.success(collegeService.list());
    }

    @PostMapping("/save")
    public ResultVO<?> save(@RequestBody College college) {
        collegeService.saveOrUpdate(college);
        return ResultVO.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO<?> delete(@PathVariable Long id) {
        collegeService.removeById(id);
        return ResultVO.success(null);
    }
} 