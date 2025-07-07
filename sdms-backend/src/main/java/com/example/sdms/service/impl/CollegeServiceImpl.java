package com.example.sdms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sdms.entity.College;
import com.example.sdms.mapper.CollegeMapper;
import com.example.sdms.service.CollegeService;
import org.springframework.stereotype.Service;

@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {
} 