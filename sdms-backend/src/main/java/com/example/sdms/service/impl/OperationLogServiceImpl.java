package com.example.sdms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sdms.entity.OperationLog;
import com.example.sdms.mapper.OperationLogMapper;
import com.example.sdms.service.OperationLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
        implements OperationLogService {
    // 可添加自定义方法

    public List<OperationLog> selectWithUserName() {
        return this.baseMapper.selectWithUserName();
    }
} 