package com.thecoloredcolors.rmsha.flikfeed.models;

import com.thecoloredcolors.rmsha.flikfeed.UserProxy;

public class Chat  {
    public String sender;

    public Chat(String sender, String senderFirebaseToken, String receiverFirebaseToken, String receiver, String senderUid, String receiverUid, String message, long timestamp) {
        this.sender = sender;
        this.senderFirebaseToken = senderFirebaseToken;
        this.receiverFirebaseToken = receiverFirebaseToken;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String senderFirebaseToken;
    public String receiverFirebaseToken;
    public String receiver;
    public String senderUid;
    public String receiverUid;
    public String message;
    public long timestamp;

    public Chat(){

    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, long timestamp){
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;

    }





}
