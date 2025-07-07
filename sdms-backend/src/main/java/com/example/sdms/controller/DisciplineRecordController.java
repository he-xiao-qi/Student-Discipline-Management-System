package com.example.sdms.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sdms.common.ResultVO;
import com.example.sdms.entity.DisciplineRecord;
import com.example.sdms.service.DisciplineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import java.util.List;

import com.example.sdms.service.StatVO;
import com.example.sdms.entity.OperationLog;
import com.example.sdms.service.OperationLogService;
import java.util.Date;
import com.example.sdms.mapper.DisciplineRecordMapper;

@RestController
@RequestMapping("/api/discipline")
public class DisciplineRecordController {
    @Autowired
    private DisciplineRecordService service;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private DisciplineRecordMapper disciplineRecordMapper;

    @GetMapping("/list")
    public ResultVO<List<DisciplineRecord>> list(@RequestParam(required = false) String college) {
        if (org.springframework.util.StringUtils.hasText(college)) {
            List<DisciplineRecord> result = disciplineRecordMapper.selectByCollege(college);
            return ResultVO.success(result);
        } else {
            return ResultVO.success(service.list());
        }
    }

    @GetMapping("/page")
    public ResultVO<IPage<DisciplineRecord>> page(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String college) {
        QueryWrapper<DisciplineRecord> qw = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.like("student_name", keyword).or().like("student_id", keyword);
        }
        if (StringUtils.hasText(college)) {
            qw.eq("college", college);
        }
        return ResultVO.success(service.page(new Page<>(page, size), qw));
    }

    @PostMapping("/save")
    public ResultVO<?> save(@RequestBody DisciplineRecord record) {
        service.saveOrUpdate(record);
        // 写入操作日志
        OperationLog log = new OperationLog();
        Long opUserId = record.getUserId();
        log.setUserId(opUserId != null ? opUserId : 1L);
        log.setAction("保存违纪记录");
        log.setTime(new java.util.Date());
        operationLogService.save(log);
        return ResultVO.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public ResultVO<?> delete(@PathVariable Long id, @RequestParam(required = false) Long userId) {
        service.removeById(id);
        // 写入操作日志
        OperationLog log = new OperationLog();
        log.setUserId(userId != null ? userId : 1L);
        log.setAction("删除违纪记录");
        log.setTime(new Date());
        operationLogService.save(log);
        return ResultVO.success(null);
    }

    @GetMapping("/stat")
    public ResultVO<List<StatVO>> stat(@RequestParam(required = false) String college) {
        if (StringUtils.hasText(college)) {
            return ResultVO.success(service.statByCollege(college));
        } else {
            return ResultVO.success(service.statByCollege());
        }
    }

    @GetMapping("/stat/type")
    public ResultVO<List<StatVO>> statByType(@RequestParam(required = false) String college) {
        if (StringUtils.hasText(college)) {
            return ResultVO.success(service.statByType(college));
        } else {
            return ResultVO.success(service.statByType());
        }
    }

    @GetMapping("/stat/date")
    public ResultVO<List<StatVO>> statByDate(@RequestParam String start, @RequestParam String end) {
        return ResultVO.success(service.statByDate(start, end));
    }

    @PostMapping("/import")
    public ResultVO<?> importBatch(@RequestBody List<DisciplineRecord> records) {
        service.saveBatch(records);
        return ResultVO.success(null);
    }

    @PostMapping("/batchDelete")
    public ResultVO<?> batchDelete(@RequestBody List<Long> ids) {
        service.removeByIds(ids);
        return ResultVO.success(null);
    }

    @GetMapping("/ai/hotTime")
    public ResultVO<List<StatVO>> getHotTime(@RequestParam String period) {
        if ("month".equals(period)) {
            return ResultVO.success(service.statByMonth());
        } else if ("weekday".equals(period)) {
            return ResultVO.success(service.statByWeekday());
        }
        return ResultVO.success(java.util.Collections.emptyList());
    }

    @GetMapping("/ai/hotGroup")
    public ResultVO<java.util.Map<String, Object>> getHotGroup() {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("topStudents", service.topStudents());
        result.put("topStudentIds", service.topStudentIds());
        result.put("topColleges", service.topColleges());
        return ResultVO.success(result);
    }

    @GetMapping("/ai/warning")
    public ResultVO<java.util.List<String>> getWarnings() {
        return ResultVO.success(service.generateWarnings());
    }
} 