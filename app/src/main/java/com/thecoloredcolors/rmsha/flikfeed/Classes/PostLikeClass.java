package com.thecoloredcolors.rmsha.flikfeed.Classes;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;

import java.io.Serializable;

/**
 * Created by rmsha on 12/2/2017.
 */

public class PostLikeClass implements Serializable {
    private String postid;
    private String userid;
    private String time;
    private UserProxyClass userProxy;

    public PostLikeClass() {
    }

    public PostLikeClass(String postid, String userid, String time) {
        this.postid = postid;
        this.userid = userid;
        this.time = time;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserProxyClass UserProxy() {
        return userProxy;
    }

    public void loadUserProxy(UserProxyClass userProxy) {
        this.userProxy = userProxy;
    }
}
