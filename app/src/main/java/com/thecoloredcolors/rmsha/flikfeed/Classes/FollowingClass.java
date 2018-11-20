package com.thecoloredcolors.rmsha.flikfeed.Classes;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;

import java.io.Serializable;

/**
 * Created by rmsha on 12/2/2017.
 */

public class FollowingClass implements Serializable {
    private String user1id;
    private String user2id;
    private UserProxyClass user1Proxy;
    private UserProxyClass user2Proxy;

    public FollowingClass() {
    }

    public FollowingClass(String user1id, String user2id) {
        this.user1id = user1id;
        this.user2id = user2id;
    }

    public String getUser1id() {
        return user1id;
    }

    public void setUser1id(String user1id) {
        this.user1id = user1id;
    }

    public String getUser2id() {
        return user2id;
    }

    public void setUser2id(String user2id) {
        this.user2id = user2id;
    }

    public UserProxyClass User1Proxy() {
        return user1Proxy;
    }

    public void loadUser1Proxy(UserProxyClass user1Proxy) {
        this.user1Proxy = user1Proxy;
    }

    public UserProxyClass User2Proxy() {
        return user2Proxy;
    }

    public void loadUser2Proxy(UserProxyClass user2Proxy) {
        this.user2Proxy = user2Proxy;
    }
}
