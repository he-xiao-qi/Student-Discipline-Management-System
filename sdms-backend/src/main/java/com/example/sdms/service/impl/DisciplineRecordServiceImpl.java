package com.example.sdms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sdms.entity.DisciplineRecord;
import com.example.sdms.mapper.DisciplineRecordMapper;
import com.example.sdms.service.DisciplineRecordService;
import com.example.sdms.service.StatVO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DisciplineRecordServiceImpl extends ServiceImpl<DisciplineRecordMapper, DisciplineRecord>
        implements DisciplineRecordService {
    // 可添加自定义方法

    @Override
    public List<StatVO> statByCollege() {
        return baseMapper.statByCollege();
    }

    @Override
    public List<StatVO> statByType() {
        return baseMapper.statByType();
    }

    @Override
    public List<StatVO> statByDate(String start, String end) {
        return baseMapper.statByDate(start, end);
    }

    @Override
    public List<StatVO> statByMonth() {
        return baseMapper.statByMonth();
    }

    @Override
    public List<StatVO> statByWeekday() {
        return baseMapper.statByWeekday();
    }

    @Override
    public List<StatVO> topStudents() {
        return baseMapper.topStudents();
    }

    @Override
    public List<StatVO> topStudentIds() {
        return baseMapper.topStudentIds();
    }

    @Override
    public List<StatVO> topColleges() {
        return baseMapper.topColleges();
    }

    @Override
    public List<String> generateWarnings() {
        List<String> warnings = new java.util.ArrayList<>();
        // 违纪高发月份预警
        List<StatVO> byMonth = statByMonth();
        double avg = byMonth.stream().mapToInt(StatVO::getCount).average().orElse(0);
        byMonth.stream().filter(m -> m.getCount() > avg * 1.5)
            .forEach(m -> warnings.add((m.getDate() != null ? m.getDate() : "") + " 违纪高发（高于平均）"));
        // 学院高发预警
        List<StatVO> colleges = topColleges();
        if (!colleges.isEmpty() && colleges.get(0).getCount() > avg) {
            warnings.add(colleges.get(0).getName() + " 近期违纪较多");
        }
        return warnings;
    }

    @Override
    public List<StatVO> statByCollege(String college) {
        return baseMapper.statByCollegeWithFilter(college);
    }

    @Override
    public List<StatVO> statByType(String college) {
        return baseMapper.statByTypeWithFilter(college);
    }
} 