package com.hydeng.user.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydeng.thrift.user.dto.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-07-01
 */
public class LoginFilter  implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)){
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie cookie : cookies){
                    if("token".equals(cookie.getName())){
                        token = cookie.getValue();
                    }
                }
            }
        }
        UserDTO userDTO = requestUserInfo(token);



    }

    private UserDTO requestUserInfo(String token) {
        String url = "http://127.0.0.1:8082/user/authentication";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();
        post.addHeader("token",token);
        InputStream inputStream = null;
        try {
            HttpResponse response = client.execute(post);
            inputStream = response.getEntity().getContent();
            byte[] temp = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int len = 0;
            if((len = inputStream.read(temp)) > 0){
                sb.append(new String(temp,0,len));
            }

            UserDTO userDTO = new ObjectMapper().readValue(sb.toString(),UserDTO.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {

    }
}
