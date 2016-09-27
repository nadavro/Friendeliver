package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 20/09/2016.
 */
public class Duty {

    private String lid;
    private  LookForUser lookForUser;
    private  DeliverUser delUser;
    private  String uid;
    private String key;

    public Duty(){

    }
    public Duty(LookForUser lookForUser, DeliverUser delUser,String lookforkey) {
        this.lookForUser = lookForUser;
        this.delUser = delUser;
        System.out.println(lookforkey);
        this.lid = lookforkey;
        this.uid = delUser.getUid();

    }

    public LookForUser getLookForUser() {
        return lookForUser;
    }

    public void setLookForUser(LookForUser lookForUser) {
        this.lookForUser = lookForUser;
    }

    public DeliverUser getDelUser() {
        return delUser;
    }

    public void setDelUser(DeliverUser delUser) {
        this.delUser = delUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getLid() {
        return lid;
    }
}
