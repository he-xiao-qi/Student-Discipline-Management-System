package com.example.sdms.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sdms.entity.DisciplineRecord;
import java.util.List;

public interface DisciplineRecordService extends IService<DisciplineRecord> {
    List<StatVO> statByCollege();
    List<StatVO> statByType();
    List<StatVO> statByDate(String start, String end);
    List<StatVO> statByMonth();
    List<StatVO> statByWeekday();
    List<StatVO> topStudents();
    List<StatVO> topStudentIds();
    List<StatVO> topColleges();
    List<String> generateWarnings();
    List<StatVO> statByCollege(String college);
    List<StatVO> statByType(String college);
} 