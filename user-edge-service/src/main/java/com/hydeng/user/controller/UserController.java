package com.hydeng.user.controller;

import com.hydeng.thrift.user.UserInfo;
import com.hydeng.user.response.Response;
import com.hydeng.user.thrift.ServiceProvider;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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


    @RequestMapping("/login")
    public Response login(@RequestParam("username") String username,@RequestParam("password") String password){

        //用户信息校验

        UserInfo userInfo = null;
        try {
            userInfo = serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(userInfo == null){
            return Response.USERNAME_PASSWORD_INVALID;
        }

        if(!userInfo.getPassword().equals(md5(password))){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        //生成token
        String token = genToken();

        //缓存用户信息

        return null;
    }

    private String genToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz",32);
    }

    private String randomCode(String s, int size) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for(int i =0;i<size;i++){
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
