package com.hydeng.user.response;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-06-30
 */
public class LoginResponse extends Response {

    private String token;

    public static LoginResponse success(String token){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        return loginResponse;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
