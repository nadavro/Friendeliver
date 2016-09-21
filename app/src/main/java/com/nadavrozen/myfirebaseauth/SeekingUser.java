package com.nadavrozen.myfirebaseauth;

import com.google.firebase.database.Exclude;

/**
 * Created by Israel Rozen on 18/09/2016.
 */


public class SeekingUser {
    private String uid;
    private User user;
    private Delivery delivery;


    public SeekingUser(String uid, User me, Delivery delivery) {
        this.uid = uid;
        this.user = me;
        this.delivery = delivery;

    }
    public SeekingUser(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
