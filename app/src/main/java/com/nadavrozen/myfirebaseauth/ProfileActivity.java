package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends Fragment implements View.OnClickListener {
    private static final String TAG = "myApp";
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private Button logbutton, wantToDeliverButton, lookForButton;
    private String userName;
    private DatabaseReference mDatabase;
    private Button myDuties;
    private View myDeliveries;
    private Button myRequests;
    private TextView uri;
    User me;
    private AccessTokenTracker mAccessTokenTracker;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile, container, false);


        //Firebase.setAndroidContext(this);

        textView = (TextView) view.findViewById(R.id.textView2);
        //Firebase usersRef = new Firebase("https://myfirstfirebaseauth.firebaseio.com");
        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,LoginActivity.class));
//        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //final Firebase ref = usersRef.child("User").child(user.getUid());

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        if (AccessToken.getCurrentAccessToken() != null) {
            mAccessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    mAccessTokenTracker.stopTracking();
                    if(currentAccessToken == null) {
                        //(the user has revoked your permissions -
                        //by going to his settings and deleted your app)
                        //do the simple login to FaceBook
                    }
                    else {
                        //you've got the new access token now.
                        //AccessToken.getToken() could be same for both
                        //parameters but you should only use "currentAccessToken"
                    }
                }
            };
            AccessToken.refreshCurrentAccessTokenAsync();
        }
        else {
            //do the simple login to FaceBook
        }


        //Toast.makeText(ProfileActivity.this,ref.g.toString(),Toast.LENGTH_SHORT).show();
        mDatabase.child("User").child(user.getUid()).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(User.class);

                userName = me.getFullName();
               String s = userName.substring(0, userName.lastIndexOf(" "));


                uri.setText(me.getFullName());

                textView.setText("Welcome " + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        uri = (TextView)view.findViewById(R.id.uri);
        logbutton = (Button) view.findViewById(R.id.buttonLogOut);
        wantToDeliverButton = (Button) view.findViewById(R.id.deliverButton);
        lookForButton = (Button) view.findViewById(R.id.lookForButton);




//        myDuties.setOnClickListener(this);
        logbutton.setOnClickListener(this);
        wantToDeliverButton.setOnClickListener(this);
        lookForButton.setOnClickListener(this);
        uri.setOnClickListener(this);
//        myDeliveries.setOnClickListener(this);
//        myRequests.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == logbutton) {
            //logout
            firebaseAuth.signOut();
            getActivity().finish();
//            Fragment fragment = new LoginActivity();
//            getFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        }

        //want-to-be-a-deliver guy activity
        if (v == wantToDeliverButton) {
            Fragment fragment = new DeliverActivity();
            getFragmentManager().beginTransaction().
                    setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            // startActivity(new Intent(this,DeliverActivity.class));
        }
//
        //looking-for-delivery activity
        if (v == lookForButton) {
            Fragment fragment = new LookingForActivity();

            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            //startActivity(new Intent(this,LookingForActivity.class));
        }

        if (v == uri){
            Uri uri = Uri.parse(me.getUri()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}
