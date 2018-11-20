package com.thecoloredcolors.rmsha.flikfeed.Classes;

import android.graphics.PointF;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;

import java.io.Serializable;

/**
 * Created by rmsha on 10/25/2017.
 */

public class TagClass implements Serializable {
    private String userid;
    private UserProxyClass tag;
    private PointF pin;
    private int postresourceindex;

    public TagClass(String userid, PointF pin, int postresourceindex) {
        this.userid = userid;
        this.pin = pin;
        this.postresourceindex = postresourceindex;
    }

    public TagClass() {
    }

    public UserProxyClass Tag() {
        return tag;
    }

    public void loadTag(UserProxyClass tag) {
        this.tag = tag;
    }

    public PointF getPin() {
        return pin;
    }

    public void setPin(PointF pin) {
        this.pin = pin;
    }

    public int getPostresourceindex() {
        return postresourceindex;
    }

    public void setPostresourceindex(int postresourceindex) {
        this.postresourceindex = postresourceindex;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
