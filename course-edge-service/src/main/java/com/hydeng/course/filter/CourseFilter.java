package com.hydeng.course.filter;

import com.hydeng.thrift.user.dto.UserDTO;
import com.hydeng.user.client.LoginFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description:
 *
 * @author hydeng
 * Date: 2018-07-10
 * Time: 17:50
 */
@Component
public class CourseFilter extends LoginFilter{
    @Override
    protected void login(HttpServletRequest request, HttpServletResponse response, UserDTO userDTO) {
        request.setAttribute("user",userDTO);
    }
}
