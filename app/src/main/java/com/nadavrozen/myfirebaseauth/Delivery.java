package com.nadavrozen.myfirebaseauth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

/**
 * Created by Israel Rozen on 06/09/2016.
 */
public class Delivery implements Parcelable {

    private String lookingUserid;
    private String strOrigin,strDest,desc;
    private String status = "PENDING";
    private String cityDepart;
    private String cityArrive;
    private String deliverUserUid;
    private String key;

    @Exclude
    public String getKey(){
        return key;
    }
    public void setKey(String key){
        this.key = key;

    }


    public Delivery(String strOrigin, String strDest, String desc, String uid) {
        this.strOrigin  = strOrigin;
        this.strDest = strDest;
        this.desc = desc;
        this.lookingUserid = uid;
        this.deliverUserUid = "";
        this.cityDepart = ParseCity(strOrigin);
        this.cityArrive = ParseCity(strDest);

    }
    public Delivery(){


    }

    protected Delivery(Parcel in) {
        lookingUserid = in.readString();
        strOrigin = in.readString();
        strDest = in.readString();
        desc = in.readString();
        status = in.readString();
        cityDepart = in.readString();
        cityArrive = in.readString();
        deliverUserUid = in.readString();
        key = in.readString();
    }

    public static final Creator<Delivery> CREATOR = new Creator<Delivery>() {
        @Override
        public Delivery createFromParcel(Parcel in) {
            return new Delivery(in);
        }

        @Override
        public Delivery[] newArray(int size) {
            return new Delivery[size];
        }
    };


    public String getStrOrigin() {
        return strOrigin;
    }

    public void setStrOrigin(String strOrigin) {
        this.strOrigin = strOrigin;
    }

    public String getStrDest() {
        return strDest;
    }

    public void setStrDest(String strDest) {
        this.strDest = strDest;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getLookingUserid() {
        return lookingUserid;
    }

    public String getCityDepart() {
        return cityDepart;
    }

    public void setCityDepart(String cityDepart) {
        this.cityDepart = cityDepart;
    }

    public String getCityArrive() {
        return cityArrive;
    }

    public void setCityArrive(String cityArrive) {
        this.cityArrive = cityArrive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lookingUserid);
        dest.writeString(strOrigin);
        dest.writeString(strDest);
        dest.writeString(desc);
        dest.writeString(status);
        dest.writeString(cityDepart);
        dest.writeString(cityArrive);
        dest.writeString(deliverUserUid);
        dest.writeString(key);
    }

    /**
     * return the City from the whole address
     * @param address
     * @return
     */
    private String ParseCity(String address) {
        if (address.indexOf(',') == address.lastIndexOf(',')){
            return address.substring(0,address.indexOf(',')).trim();

        }
        return address.substring(address.indexOf(',')+1,address.lastIndexOf(',')).trim();

    }


    public String getDeliverUserUid() {
        return deliverUserUid;
    }

    public void setDeliverUserUid(String deliverUserUid) {
        this.deliverUserUid = deliverUserUid;
    }
}
