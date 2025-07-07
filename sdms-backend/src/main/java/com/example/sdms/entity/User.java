package com.example.sdms.entity;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name; // 姓名
    private String jobId; // 工号/学号
    private String username;
    private String password;
    private String roles; // 多角色，逗号分隔
    private String colleges; // 多学院，逗号分隔
    private String status; // active/frozen
    private Date addTime; // 添加时间
    // 兼容旧字段
    // private String role;
    // private String college;
} 