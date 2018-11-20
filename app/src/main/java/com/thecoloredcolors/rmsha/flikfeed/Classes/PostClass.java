package com.thecoloredcolors.rmsha.flikfeed.Classes;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;
import com.thecoloredcolors.rmsha.flikfeed.Helpers.FireBaseHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.aflak.filter_annotation.Filterable;

/**
 * Created by rmsha on 10/22/2017.
 */

public class PostClass implements Serializable {

    private String userid;
    private String postid;
    private String caption;
    private String time;
    private UserProxyClass posteduser;
    private List<PostResourceClass> resources = new ArrayList<>();
    private int numofreports;
    private int numoflikes;
    private int numofcomments;

    public PostClass() {
    }

    public PostClass(String postid, String caption, String time, String userid, List<PostResourceClass> resources, int numoflikes, int numofcomments) {
        this.postid = postid;
        this.caption = caption;
        this.time = time;
        this.userid = userid;
        this.resources = resources;
        this.numoflikes = numoflikes;
        this.numofcomments = numofcomments;
    }

    public int NumofResources() {
        return resources.size();
    }

    public int getNumofreports() {
        return numofreports;
    }

    public void setNumofreports(int numofreports) {
        this.numofreports = numofreports;
    }

    public void AddReport() {
        this.numofreports++;
    }

    public PostProxyClass PostProxy() {
        return new PostProxyClass(postid, userid, resources.get(0),time);
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserProxyClass Posteduser() {
        return posteduser;
    }

    public void loadPosteduser(UserProxyClass posteduser) {
        this.posteduser = posteduser;
    }

    public List<PostResourceClass> getResources() {
        return resources;
    }

    public void setResources(ArrayList<PostResourceClass> resources) {
        this.resources = resources;
    }

    public void AddResource(PostResourceClass postResource) {
        resources.add(postResource);
    }

    public int getNumoflikes() {
        return numoflikes;
    }

    public void setNumoflikes(int numoflikes) {
        this.numoflikes = numoflikes;
    }

    public int getNumofcomments() {
        return numofcomments;
    }

    public void setNumofcomments(int numofcomments) {
        this.numofcomments = numofcomments;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
