package com.hydeng.user.controller;

import com.hydeng.thrift.user.UserInfo;
import com.hydeng.thrift.user.dto.UserDTO;
import com.hydeng.user.redis.RedisClient;
import com.hydeng.user.response.LoginResponse;
import com.hydeng.user.response.Response;
import com.hydeng.user.thrift.ServiceProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.util.Random;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-26
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private ServiceProvider serviceProvider;

    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "/login";
    }

    @RequestMapping(value = "/sendVerifyCode", method = RequestMethod.POST)
    @ResponseBody
    public Response sendVerifyCode(@RequestParam(value = "email", required = false) String email,
                                   @RequestParam(value = "mobile", required = false) String mobile) {
        String message = "Verify Code is :";
        String code = randomCode("0123456789", 6);
        try {
            boolean result = false;
            if (StringUtils.isNotBlank(mobile)) {
                result = serviceProvider.getMessageService().sendMobileMessage(mobile, message + code);
                redisClient.set(mobile, code);
            } else if (StringUtils.isNotBlank(email)) {
                result = serviceProvider.getMessageService().sendEmailMessage(email, message + code);
                redisClient.set(email, code);
            } else {
                return Response.MOBILE_OR_EMAIL_REQUIRED;
            }
            if (!result) {
                return Response.SEND_VERIFYCODE_FAILED;
            }
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }
        return Response.SUCCESS;
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam("verifyCode") String verifyCode) {

        if (StringUtils.isBlank(email) && StringUtils.isBlank(mobile)) {
            return Response.MOBILE_OR_EMAIL_REQUIRED;
        }

        if(StringUtils.isNotBlank(mobile)) {
            String redisCode = redisClient.get(mobile);
            if(!verifyCode.equals(redisCode)) {
                return Response.VERIFY_CODE_INVALID;
            }
        }else {
            String redisCode = redisClient.get(email);
            if(!verifyCode.equals(redisCode)) {
                return Response.VERIFY_CODE_INVALID;
            }
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(md5(password));
        userInfo.setMobile(mobile);
        userInfo.setEmail(email);
        try {
            serviceProvider.getUserService().registerUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }
        return Response.SUCCESS;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username") String username, @RequestParam("password") String password) {

        //用户信息校验

        UserInfo userInfo = null;
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if (userInfo == null) {
            return Response.USERNAME_PASSWORD_INVALID;
        }

        if (!userInfo.getPassword().equals(md5(password))) {
            return Response.USERNAME_PASSWORD_INVALID;
        }
        //生成token
        String token = genToken();

        //缓存用户信息
        redisClient.set(token, toDto(userInfo), 3600);
        return LoginResponse.success(token);
    }

    private UserDTO toDto(UserInfo userInfo) {
        UserDTO userDto = new UserDTO();
        BeanUtils.copyProperties(userInfo, userDto);
        return userDto;
    }

    private String genToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz", 32);
    }

    private String randomCode(String s, int size) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int loc = random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }

    private String md5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
