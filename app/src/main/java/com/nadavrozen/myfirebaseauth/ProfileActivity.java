package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Intent;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);


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


        //Toast.makeText(ProfileActivity.this,ref.g.toString(),Toast.LENGTH_SHORT).show();
        mDatabase.child("User").child(user.getUid()).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(User.class).getFullName();
                String s = userName.substring(0, userName.lastIndexOf(" "));

                textView.setText("Welcome " + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        logbutton = (Button) view.findViewById(R.id.buttonLogOut);
        wantToDeliverButton = (Button) view.findViewById(R.id.deliverButton);
        lookForButton = (Button) view.findViewById(R.id.lookForButton);
//        myDuties = (Button) view.findViewById(R.id.myDutiesBtn);
//        myDeliveries = (Button) view.findViewById(R.id.myDeliveriesBtn);
//        myRequests = (Button) view.findViewById(R.id.myRequestBtn);


//        myDuties.setOnClickListener(this);
        logbutton.setOnClickListener(this);
        wantToDeliverButton.setOnClickListener(this);
        lookForButton.setOnClickListener(this);
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
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            // startActivity(new Intent(this,DeliverActivity.class));
        }
//
        //looking-for-delivery activity
        if (v == lookForButton) {
            Fragment fragment = new LookingForActivity();

            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            //startActivity(new Intent(this,LookingForActivity.class));
        }


    }

}
