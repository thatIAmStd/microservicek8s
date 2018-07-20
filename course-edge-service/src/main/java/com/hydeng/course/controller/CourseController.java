package com.hydeng.course.controller;

import com.hydeng.course.dto.CourseDTO;
import com.hydeng.course.service.ICourseService;
import com.hydeng.thrift.user.dto.UserDTO;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 *
 * @author hydeng
 * Date: 2018-07-10
 * Time: 17:33
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    @Reference
    private ICourseService iCourseService;

    @RequestMapping(value = "/courseList",method = RequestMethod.POST)
    List<CourseDTO> courseList(HttpServletRequest request){
        UserDTO userDTO = (UserDTO) request.getAttribute("user");
        System.out.println(userDTO);
        return iCourseService.courseList();
    }
}
