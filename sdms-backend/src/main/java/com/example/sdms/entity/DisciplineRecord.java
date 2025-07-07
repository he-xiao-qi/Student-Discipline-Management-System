package com.example.sdms.entity;
import lombok.Data;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

@Data
public class DisciplineRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String studentName;
    private String studentId;
    private String college;
    private String type;
    private Date date;
    private String punishment;
    private Long userId;
} 