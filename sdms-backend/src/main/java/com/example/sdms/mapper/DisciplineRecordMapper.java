package com.example.sdms.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sdms.entity.DisciplineRecord;
import com.example.sdms.service.StatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;
 
@Mapper
public interface DisciplineRecordMapper extends BaseMapper<DisciplineRecord> {
    @Select("SELECT college AS name, COUNT(*) AS count FROM discipline_record GROUP BY college")
    List<StatVO> statByCollege();

    @Select("SELECT type AS name, COUNT(*) AS count FROM discipline_record GROUP BY type")
    List<StatVO> statByType();

    @Select("SELECT DATE_FORMAT(date, '%Y-%m') AS date, COUNT(*) AS count FROM discipline_record WHERE date BETWEEN #{start} AND #{end} GROUP BY DATE_FORMAT(date, '%Y-%m')")
    List<StatVO> statByDate(String start, String end);

    @Select("SELECT DATE_FORMAT(date, '%Y-%m') AS date, COUNT(*) AS count FROM discipline_record GROUP BY DATE_FORMAT(date, '%Y-%m')")
    List<StatVO> statByMonth();

    @Select("SELECT DAYOFWEEK(date) AS name, COUNT(*) AS count FROM discipline_record GROUP BY DAYOFWEEK(date)")
    List<StatVO> statByWeekday();

    @Select("SELECT student_name AS name, COUNT(*) AS count FROM discipline_record GROUP BY student_name ORDER BY count DESC LIMIT 10")
    List<StatVO> topStudents();

    @Select("SELECT student_id AS name, COUNT(*) AS count FROM discipline_record GROUP BY student_id ORDER BY count DESC LIMIT 10")
    List<StatVO> topStudentIds();

    @Select("SELECT college AS name, COUNT(*) AS count FROM discipline_record GROUP BY college ORDER BY count DESC LIMIT 10")
    List<StatVO> topColleges();

    @Select({
      "<script>",
      "SELECT college AS name, COUNT(*) AS count FROM discipline_record",
      "<where>",
      "  <if test='college != null and college != \"\"'>",
      "    college = #{college}",
      "  </if>",
      "</where>",
      "GROUP BY college",
      "</script>"
    })
    List<StatVO> statByCollegeWithFilter(@org.apache.ibatis.annotations.Param("college") String college);

    @Select({
      "<script>",
      "SELECT type AS name, COUNT(*) AS count FROM discipline_record",
      "<where>",
      "  <if test='college != null and college != \"\"'>",
      "    college = #{college}",
      "  </if>",
      "</where>",
      "GROUP BY type",
      "</script>"
    })
    List<StatVO> statByTypeWithFilter(@org.apache.ibatis.annotations.Param("college") String college);

    @Select("SELECT * FROM discipline_record WHERE college = #{college}")
    List<DisciplineRecord> selectByCollege(@Param("college") String college);
} 