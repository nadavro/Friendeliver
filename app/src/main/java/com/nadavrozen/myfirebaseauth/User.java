package com.nadavrozen.myfirebaseauth;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Israel Rozen on 01/09/2016.
 */
public class User implements Parcelable {

    private  int isFacebook;
    private String email;
    private String birthday = "";
    String fullName;
    private String uriImage = "";
    private String phone = "";
    private String facebookLink="";


    public User() {}

    public User(String fullName, String birthday ,String email,int isFacebook) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.email = email;
        this.isFacebook = isFacebook;
    }

    public User(User me) {
        this.email = me.email;
        this.birthday = me.birthday;
        this.fullName = me.fullName;
        this.isFacebook=  me.isFacebook;
    }

    protected User(Parcel in) {
        email = in.readString();
        birthday = in.readString();
        fullName = in.readString();
        phone = in.readString();
        isFacebook = in.readInt();
        facebookLink = in.readString();
        uriImage = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getBirthday() {return birthday;}

        public String getFullName() {
            return fullName;
        }

    public String getEmail() {
        return email;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeString(fullName);
        dest.writeString(phone);
        dest.writeString(uriImage);
        dest.writeInt(isFacebook);
        dest.writeString(facebookLink);
    }

    public String getPhone() {
        return phone;
    }


    public int getIsFacebook() {
        return isFacebook;
    }

    public void setIsFacebook(int isFacebook) {
        this.isFacebook = isFacebook;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }
}
