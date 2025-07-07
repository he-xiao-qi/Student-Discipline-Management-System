package com.example.sdms.entity;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@Data
public class College {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
} 