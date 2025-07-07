package com.example.sdms.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sdms.entity.OperationLog;
import java.util.List;

public interface OperationLogService extends IService<OperationLog> {
    List<OperationLog> selectWithUserName();
} 