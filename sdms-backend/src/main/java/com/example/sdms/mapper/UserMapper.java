package com.example.sdms.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sdms.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
 
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 只更新非密码字段
     */
    @Update("UPDATE user SET name=#{user.name}, job_id=#{user.jobId}, username=#{user.username}, roles=#{user.roles}, colleges=#{user.colleges}, status=#{user.status} WHERE id=#{user.id}")
    int updateUserWithoutPassword(@Param("user") User user);
} 