package com.nadavrozen.myfirebaseauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private Button logbutton,wantToDeliverButton,lookForButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        textView = (TextView)findViewById(R.id.textView2);
        textView.setText("welcome "+user.getEmail());
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
            finish();
            startActivity(new Intent(this,LookingForActivity.class));
        }



    }
}
