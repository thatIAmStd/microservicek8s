package com.hydeng.course.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.hydeng.course.dto.CourseDTO;
import com.hydeng.course.service.ICourseService;
import com.hydeng.thrift.user.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 *
 * @author hydeng
 * Date: 2018-07-10
 * Time: 17:33
 */
@Controller
@RequestMapping("/course")
public class CourseController {

    @Reference
    private ICourseService iCourseService;

    @RequestMapping(value = "/courseList",method = RequestMethod.POST)
    @ResponseBody
    List<CourseDTO> courseList(HttpServletRequest request){
        UserDTO userDTO = (UserDTO) request.getAttribute("user");
        System.out.println(userDTO);
        return iCourseService.courseList();
    }
}
