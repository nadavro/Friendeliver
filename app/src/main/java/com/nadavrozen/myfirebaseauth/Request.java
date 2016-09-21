package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 18/09/2016.
 */
public class Request {

    //private LookForUser lookForUser;
    private String status = "WAITING";
    private String lookUserID;
    private String delUserID;
    private String delUserUID;
    private String lookUserUID;
    private String deliveryID;
    private String key;


    public Request() {

    }
    public Request(LookForUser lookForUser, DeliverUser object,String delkey){
        this.deliveryID = delkey;
        System.out.println("IN MY REQUEST "+deliveryID);
        this.delUserID = object.getKey();
        this.lookUserID = lookForUser.getKey();
        this.lookUserUID = lookForUser.getUid();
        this.delUserUID = object.getUid();
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLookUserID() {
        return lookUserID;
    }

    public void setLookUserID(String lookUserID) {
        this.lookUserID = lookUserID;
    }

    public String getDelUserID() {
        return delUserID;
    }

    public void setDelUserID(String delUserID) {
        this.delUserID = delUserID;
    }

    public String getDelUserUID() {
        return delUserUID;
    }

    public void setDelUserUID(String delUserUID) {
        this.delUserUID = delUserUID;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getLookUserUID() {
        return lookUserUID;
    }

    public void setLookUserUID(String lookUserUID) {
        this.lookUserUID = lookUserUID;
    }
}
