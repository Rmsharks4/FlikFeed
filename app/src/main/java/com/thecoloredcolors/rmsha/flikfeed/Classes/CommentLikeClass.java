package com.thecoloredcolors.rmsha.flikfeed.Classes;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;

import java.io.Serializable;

/**
 * Created by rmsha on 12/2/2017.
 */

public class CommentLikeClass implements Serializable {
    private String commentid;
    private String userid;
    private String time;
    private UserProxyClass userProxy;
    private String postid;

    public CommentLikeClass() {
    }

    public CommentLikeClass(String commentid, String userid, String time) {
        this.commentid = commentid;
        this.userid = userid;
        this.time = time;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
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

    public String Postid() {
        return postid;
    }

    public void loadPostid(String postid) {
        this.postid = postid;
    }
}
