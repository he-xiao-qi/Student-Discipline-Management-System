package com.example.sdms.entity;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

@Data
public class PunishmentType {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
} 