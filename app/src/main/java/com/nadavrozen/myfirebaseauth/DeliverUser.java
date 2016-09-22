package com.nadavrozen.myfirebaseauth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by Israel Rozen on 06/09/2016.
 */
public class DeliverUser implements Parcelable {

    private User user;
    private String email;
    private String birthday;
    private String name;
    private String cityArrive;
    private String cityDepart;
    private String uid = null;
    private String dateStr;
    private String arriveStr;
    private String departStr;
    private String arriveAtStr;
    private String departAtStr;
    private String key;
    private ArrayList<Request> Requests=new ArrayList<Request>();
    private ArrayList<Delivery> deliveries = new ArrayList<>();
    private String request;


    public DeliverUser(){

    }

    protected DeliverUser(Parcel in) {
        email = in.readString();
        birthday = in.readString();
        name = in.readString();
        cityArrive = in.readString();
        cityDepart = in.readString();
        uid = in.readString();
        dateStr = in.readString();
        arriveStr = in.readString();
        departStr = in.readString();
        arriveAtStr = in.readString();
        departAtStr = in.readString();
        key = in.readString();
        deliveries = in.createTypedArrayList(Delivery.CREATOR);
        request = in.readString();
    }

    public static final Creator<DeliverUser> CREATOR = new Creator<DeliverUser>() {
        @Override
        public DeliverUser createFromParcel(Parcel in) {
            return new DeliverUser(in);
        }

        @Override
        public DeliverUser[] newArray(int size) {
            return new DeliverUser[size];
        }
    };

    @Exclude
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;

    }

    public DeliverUser(User me,String dateStr, String arriveStr, String departStr, String arriveAtStr,
                       String departAtStr, String uid) {
        this.user = me;
        this.dateStr = dateStr;
        this.arriveStr = arriveStr;
        this.departStr = departStr;
        this.arriveAtStr = arriveAtStr;
        this.departAtStr = departAtStr;
        this.uid = uid;

        this.cityDepart = ParseCity(departStr);
        this.cityArrive = ParseCity(arriveStr);

    }

    /**
     * return the City from the whole address
     * @param address
     * @return
     */
    private String ParseCity(String address) {
        if (address.indexOf(',') == address.lastIndexOf(',')){
            return address.substring(address.indexOf(',')+1).trim();

        }
        return address.substring(address.indexOf(',')+1,address.lastIndexOf(',')).trim();

    }

    public String getUid() {
        return uid;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getArriveStr() {
        return arriveStr;
    }

    public void setArriveStr(String arriveStr) {
        this.arriveStr = arriveStr;
    }

    public String getDepartStr() {
        return departStr;
    }

    public void setDepartStr(String departStr) {
        this.departStr = departStr;
    }

    public String getArriveAtStr() {
        return arriveAtStr;
    }

    public void setArriveAtStr(String arriveAtStr) {
        this.arriveAtStr = arriveAtStr;
    }

    public String getDepartAtStr() {
        return departAtStr;
    }

    public void setDepartAtStr(String departAtStr) {
        this.departAtStr = departAtStr;
    }

    public String getCityArrive() {
        return cityArrive;
    }

    public void setCityArrive(String cityArrive) {
        this.cityArrive = cityArrive;
    }

    public String getCityDepart() {
        return cityDepart;
    }

    public void setCityDepart(String cityDepart) {
        this.cityDepart = cityDepart;
    }


    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public ArrayList<Delivery> getDeliveries(){
        return this.deliveries;
    }

    public void appendDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }

    public String getRequest() {
        return request;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public ArrayList<Request> getRequests() {
        return Requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        Requests = requests;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeString(name);
        dest.writeString(cityArrive);
        dest.writeString(cityDepart);
        dest.writeString(uid);
        dest.writeString(dateStr);
        dest.writeString(arriveStr);
        dest.writeString(departStr);
        dest.writeString(arriveAtStr);
        dest.writeString(departAtStr);
        dest.writeString(key);
        dest.writeTypedList(deliveries);
        dest.writeString(request);
    }
}
