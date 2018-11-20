package com.thecoloredcolors.rmsha.flikfeed;

import java.io.Serializable;

/**
 * Created by rmsha on 10/25/2017.
 */

public class UserProxy implements Serializable {

    private String userid;
    private String username;
    private String profilepic;
    private boolean hasstories;

    public UserProxy(String userid, String username, String profilepic, boolean hasstories) {
        this.userid = userid;
        this.username = username;
        this.profilepic = profilepic;
        this.hasstories = hasstories;
    }

    public UserProxy() {

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
}
