package com.example.sdms.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sdms.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
    @Select("SELECT log.*, u.name as userName FROM operation_log log LEFT JOIN user u ON log.user_id = u.id")
    List<OperationLog> selectWithUserName();
} 