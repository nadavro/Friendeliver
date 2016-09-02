package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 01/09/2016.
 */
public class User {

        private String email;
        private String birthday;
        private String fullName;

        public User() {}

        public User(String fullName, String birthday , String email) {
            this.fullName = fullName;
            this.birthday = birthday;
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getFullName() {
            return fullName;
        }

    public String getEmail() {
        return email;
    }
}
