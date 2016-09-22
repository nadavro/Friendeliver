package com.nadavrozen.myfirebaseauth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Israel Rozen on 01/09/2016.
 */
public class User implements Parcelable {

        private String email;
        private String birthday = "";
         String fullName;
        private String uri = "";

        public User() {}

        public User(String fullName, String birthday , String email, String uri) {
            this.fullName = fullName;
            this.birthday = birthday;
            this.email = email;
            this.uri = uri;
        }

    public User(User me) {
        this.email = me.email;
        this.birthday = me.birthday;
        this.fullName = me.fullName;
    }

    protected User(Parcel in) {
        email = in.readString();
        birthday = in.readString();
        fullName = in.readString();
        uri = in.readString();
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

    public String getBirthday() {
        System.out.println(birthday);
        return birthday;
        }

        public String getFullName() {
            return fullName;
        }

    public String getEmail() {
        return email;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
        dest.writeString(uri);
    }
}
