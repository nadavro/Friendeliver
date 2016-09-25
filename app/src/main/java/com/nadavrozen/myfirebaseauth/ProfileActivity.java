package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

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
        final String key = user.getUid();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference ry = mDatabase.child("User").child(key).child("fcmToken");
        ry.setValue(refreshedToken);

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
                System.out.println("innnnn profileeeeeeeeee");
                me = dataSnapshot.getValue(User.class);

                userName = me.getFullName();


                if (me.getPhone().equals("")){
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
                    dialogBuilder.setTitle("ENTER PHONE").setMessage("Please enter your phone number");
// ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    View dialogView = inflater.inflate(R.layout.phone_dialog, null);
                    dialogBuilder.setView(dialogView);
                    final EditText editText = (EditText) dialogView.findViewById(R.id.phone);

                    //System.out.println(f);
//                    DatabaseReference r = mDatabase.child("User").child(key).child("phone");
//                    r.setValue("93939393");
                    dialogBuilder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String f = String.valueOf(editText.getText());
                            DatabaseReference r = mDatabase.child("User").child(key).child("phone");
                            r.setValue(f);

                        }
                    });

                   // dialogBuilder.setView(dialogView);
                    //dialogBuilder.setPositiveButton("DONE",ne)




//                    editText.setText("Please enter phone number");
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();


                }

                //uri.setText(me.getFullName());

                //textView.setText("Welcome " + s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        logbutton = (Button) view.findViewById(R.id.buttonLogOut);
        wantToDeliverButton = (Button) view.findViewById(R.id.deliverButton);
        lookForButton = (Button) view.findViewById(R.id.lookForButton);




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


    }

}
