package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 01/09/2016.
 */
public class User {

        private String email;
        private String birthday = "";
         String fullName;

        public User() {}

        public User(String fullName, String birthday , String email) {
            this.fullName = fullName;
            this.birthday = birthday;
            this.email = email;
        }

    public User(User me) {
        this.email = me.email;
        this.birthday = me.birthday;
        this.fullName = me.fullName;
    }

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
}
