package com.thecoloredcolors.rmsha.flikfeed.Classes;

import java.io.Serializable;

/**
 * Created by rmsha on 10/25/2017.
 */

public class PostProxyClass implements Serializable {
    private String postid;
    private String userid;
    private PostResourceClass postResource;
    private String time;

    public PostProxyClass(String postid, String userid, PostResourceClass postResource, String time) {
        this.postid = postid;
        this.userid = userid;
        this.postResource = postResource;
        this.time = time;
    }

    public PostProxyClass() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public PostResourceClass getPostResource() {
        return postResource;
    }

    public void setPostResource(PostResourceClass postResource) {
        this.postResource = postResource;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
