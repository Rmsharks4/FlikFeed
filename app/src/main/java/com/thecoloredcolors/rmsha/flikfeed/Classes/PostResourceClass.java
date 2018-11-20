package com.thecoloredcolors.rmsha.flikfeed.Classes;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmsha on 10/25/2017.
 */

public class PostResourceClass implements Serializable {
    private String resource;
    private int resourcetype;

    public static final int IMAGE = 0;
    public static final int VIDEO = 1;

    public PostResourceClass(String resource, int resourcetype) {
        this.resource = resource;
        this.resourcetype = resourcetype;
    }

    public PostResourceClass() {
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public int getResourcetype() {
        return resourcetype;
    }

    public void setResourcetype(int resourcetype) {
        this.resourcetype = resourcetype;
    }

}
