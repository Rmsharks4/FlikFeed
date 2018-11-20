package com.thecoloredcolors.rmsha.flikfeed.Classes;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.models.User;

/**
 * Created by rmsha on 10/25/2017.
 */

public class NotificationClass {
    private String userid;
    private String postid;
    private int type;
    private String notifuserid;
    private String time;
    private UserProxyClass NotifUserProxy;

    public static final int POST_LIKE = 0;
    public static final int COMMENT = 1;
    public static final int COMMENT_LIKE = 2;

    public NotificationClass(String userid, String postid, int type, String notifuserid, String time) {
        this.userid = userid;
        this.postid = postid;
        this.type = type;
        this.notifuserid = notifuserid;
        this.time = time;
    }

    public NotificationClass() {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNotifuserid() {
        return notifuserid;
    }

    public void setNotifuserid(String notifuserid) {
        this.notifuserid = notifuserid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserProxyClass NotifUserProxy() {
        return NotifUserProxy;
    }

    public void loadNotifUserProxy(UserProxyClass notifUserProxy) {
        NotifUserProxy = notifUserProxy;
    }
}
