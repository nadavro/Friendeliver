package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 22/09/2016.
 */
public class Review {
    private String name;
    private String recommendation;
    private String uid;


    public Review(){

    }
    public Review(String name,String recommendation,String uid){
        this.name = name;
        this.recommendation = recommendation;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}



