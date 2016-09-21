package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 20/09/2016.
 */
public class Duty {
    private  LookForUser lookForUser;
    private  DeliverUser delUser;
    private  String uid;

    public Duty(){

    }
    public Duty(LookForUser lookForUser, DeliverUser delUser) {
        this.lookForUser = lookForUser;
        this.delUser = delUser;
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
}
