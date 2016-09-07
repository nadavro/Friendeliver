package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "myApp";
    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private Button logbutton,wantToDeliverButton,lookForButton;
    private String userName;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Firebase.setAndroidContext(this);

        textView = (TextView)findViewById(R.id.textView2);
        //Firebase usersRef = new Firebase("https://myfirstfirebaseauth.firebaseio.com");
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        //final Firebase ref = usersRef.child("User").child(user.getUid());

        //Toast.makeText(ProfileActivity.this,ref.g.toString(),Toast.LENGTH_SHORT).show();
        mDatabase.child("User").child(user.getUid()).addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                userName = dataSnapshot.getValue(User.class).getFullName();
                String s = userName.substring(0,userName.lastIndexOf(" "));

                textView.setText("Welcome "+s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        logbutton = (Button)findViewById(R.id.buttonLogOut);
        wantToDeliverButton = (Button)findViewById(R.id.deliverButton);
        lookForButton = (Button)findViewById(R.id.lookForButton);

        logbutton.setOnClickListener(this);
        wantToDeliverButton.setOnClickListener(this);
        lookForButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == logbutton){
            //logout
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        if (v == wantToDeliverButton){
            finish();
            startActivity(new Intent(this,DeliverActivity.class));
        }

        if (v == lookForButton){

            startActivity(new Intent(this,LookingForActivity.class));
        }



    }
}
