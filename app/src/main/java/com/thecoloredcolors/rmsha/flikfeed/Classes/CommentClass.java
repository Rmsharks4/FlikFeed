package com.thecoloredcolors.rmsha.flikfeed.Classes;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;

import java.io.Serializable;

/**
 * Created by rmsha on 10/22/2017.
 */

public class CommentClass implements Serializable {

    private String commentid;
    private String postid;
    private String userid;
    private UserProxyClass userproxy;
    private String text;
    private String time;
    private int numoflikes;

    public CommentClass(String postid, String commentid, String userid, String text, String time, int numoflikes) {
        this.postid = postid;
        this.commentid = commentid;
        this.userid = userid;
        this.text = text;
        this.time = time;
        this.numoflikes = numoflikes;
    }

    public CommentClass(String postid, String userid, String text, String time, int numoflikes) {
        this.postid = postid;
        this.commentid = commentid;
        this.userid = userid;
        this.text = text;
        this.time = time;
        this.numoflikes = numoflikes;
    }

    public CommentClass() {
    }

    public String getcommentid() {
        return commentid;
    }

    public void setcommentid(String commentid) {
        this.commentid = commentid;
    }

    public UserProxyClass UserProxy() {
        return userproxy;
    }

    public void loadUserProxy(UserProxyClass userproxy) {
        this.userproxy = userproxy;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNumoflikes() {
        return numoflikes;
    }

    public void setNumoflikes(int numoflikes) {
        this.numoflikes = numoflikes;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
