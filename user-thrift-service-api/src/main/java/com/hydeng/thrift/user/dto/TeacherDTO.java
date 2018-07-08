package com.hydeng.thrift.user.dto;

/**
 * Description:
 *
 * @author: hydeng
 * @since: 2018-07-08
 */
public class TeacherDTO extends UserDTO{
    private String intro;
    private int starts;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStarts() {
        return starts;
    }

    public void setStarts(int starts) {
        this.starts = starts;
    }
}
