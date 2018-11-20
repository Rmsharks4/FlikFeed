package com.thecoloredcolors.rmsha.flikfeed.Classes;

import java.io.Serializable;

import me.aflak.filter_annotation.Filterable;

/**
 * Created by rmsha on 11/29/2017.
 */

@Filterable
public class SearchResult implements Serializable {
    private String searchid;
    private String match;
    private String sideline;
    private String picture;
    private int type;

    public static final int USER = 0;
    public static final int POST = 1;

    public SearchResult() {
    }

    public SearchResult(String searchid, String match, String sideline, String picture, int type) {
        this.searchid = searchid;
        this.match = match;
        this.sideline = sideline;
        this.picture = picture;
        this.type = type;
    }

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getSideline() {
        return sideline;
    }

    public void setSideline(String sideline) {
        this.sideline = sideline;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSearchid() {
        return searchid;
    }

    public void setSearchid(String searchid) {
        this.searchid = searchid;
    }

    public static int getUSER() {
        return USER;
    }

    public static int getPOST() {
        return POST;
    }
}
