package com.thecoloredcolors.rmsha.flikfeed.Classes.User;

import java.io.Serializable;

import me.aflak.filter_annotation.Filterable;

/**
 * Created by rmsha on 10/25/2017.
 */

public class UserProxyClass implements Serializable {

    private String userid;
    private String username;
    private String name;
    private String profilepic;
    private boolean hasstories;

    public UserProxyClass(String userid, String username, String name, String profilepic, boolean hasstories) {
        this.userid = userid;
        this.username = username;
        this.name = name;
        this.profilepic = profilepic;
        this.hasstories = hasstories;
    }

    public UserProxyClass() {
    }

    public boolean isHasstories() {
        return hasstories;
    }

    public void setHasstories(boolean hasstories) {
        this.hasstories = hasstories;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
