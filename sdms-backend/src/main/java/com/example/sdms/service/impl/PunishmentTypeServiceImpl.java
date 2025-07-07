package com.example.sdms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sdms.entity.PunishmentType;
import com.example.sdms.mapper.PunishmentTypeMapper;
import com.example.sdms.service.PunishmentTypeService;
import org.springframework.stereotype.Service;

@Service
public class PunishmentTypeServiceImpl extends ServiceImpl<PunishmentTypeMapper, PunishmentType>
        implements PunishmentTypeService {
    // 可添加自定义方法
} 