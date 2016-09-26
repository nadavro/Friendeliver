package com.nadavrozen.myfirebaseauth;

import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Israel Rozen on 06/09/2016.
 */
public class LookForUser {

    private User user;
    private Delivery delivery;
    private String uid;
    private String key;
    private String dutyId = "";
    private String delToken = "";


    public LookForUser(){

    }
    public LookForUser(User user, Delivery delivery, String uid) {
        this.user = user;
        this.delivery = delivery;
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

    public String getUid() {
        return uid;
    }

    @Exclude
    public String getKey() {
        return key;
    }
    public void setKey(String key){
        this.key = key;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getDelToken() {
        return delToken;
    }

    public void setDelToken(String delToken) {
        this.delToken = delToken;
    }
}
