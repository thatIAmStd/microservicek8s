package com.hydeng.course.mapper;

import com.hydeng.course.dto.CourseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-07-08
 */
@Mapper
public interface CourseMapper {

    @Select("select id,title,description from pe_course")
    List<CourseDTO> listCourse();

    @Select("select id from pr_user_course where course_id = #{courseId}")
    Integer  getCourseTeacher(@Param("courseId") Integer courseId );
}
