package com.thecoloredcolors.rmsha.flikfeed.Classes;

import com.thecoloredcolors.rmsha.flikfeed.Classes.User.UserProxyClass;

/**
 * Created by Alizeh Asim Akram on 29-Nov-17.
 */

public class FollowRequestClass {
    private String senderid;
    private String receiverid;
    private UserProxyClass sender;

    public FollowRequestClass(String senderid, String receiverid) {
        this.senderid = senderid;
        this.receiverid = receiverid;
    }

    public FollowRequestClass() {
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public UserProxyClass Sender() {
        return sender;
    }

    public void loadSender(UserProxyClass sender) {
        this.sender = sender;
    }

}
