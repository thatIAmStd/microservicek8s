package com.hydeng.course.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hydeng.course.mapper.CourseMapper;
import com.hydeng.course.dto.CourseDTO;
import com.hydeng.thrift.user.UserInfo;
import com.hydeng.thrift.user.dto.TeacherDTO;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-07-08
 */
@Service
public class CourseServiceImpl implements ICourseService{

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ServiceProvider serviceProvider;

    @Override
    public List<CourseDTO> courseList() {
        List<CourseDTO> courseDTOS = courseMapper.listCourse();
        if(courseDTOS != null){
            for(CourseDTO courseDTO : courseDTOS){
                Integer teacherId = courseMapper.getCourseTeacher(courseDTO.getId());
                if(teacherId != null){
                    try {
                        UserInfo userInfo = serviceProvider.getUserService().getTeacherById(teacherId);
                        courseDTO.setTeacher(trans2Teacher(userInfo));
                    } catch (TException e) {
                        e.printStackTrace();
                        return null;
                    }
                }

            }
        }
        System.out.println(courseDTOS);
        return courseDTOS;
    }

    private TeacherDTO trans2Teacher(UserInfo userInfo) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(userInfo,teacherDTO);
        return teacherDTO;
    }
}
