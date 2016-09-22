package com.nadavrozen.myfirebaseauth;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * The profile of the user including image,reviews and other personal detail
 */
public class UserProfile extends Fragment {
    private User currentUser;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_profile, container, false);


        //getting the currentUser from the previews fragment
        Bundle extras = this.getArguments();
        if (extras != null) {
            currentUser = extras.getParcelable("User");

        }

        System.out.println("IN USER PROFILE" + currentUser.getFullName());
        return  view;
    }
}
