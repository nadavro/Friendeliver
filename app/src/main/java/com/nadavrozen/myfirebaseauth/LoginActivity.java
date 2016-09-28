package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    FirebaseHelper firebaseHelper;
    private DatabaseReference db;


    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        TextView title = (TextView)findViewById(R.id.logo);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/PoiretOne-Regular.ttf");

        title.setTypeface(custom_font);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    startActivity(new Intent(getApplicationContext(), ContainerActivity.class));

                }
                else {

                }
            }
        };

        db= FirebaseDatabase.getInstance().getReference();
        firebaseHelper=new FirebaseHelper(db);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        if (firebaseAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ContainerActivity.class));

        }
        progressDialog = new ProgressDialog(this);
        buttonSignin = (Button) findViewById(R.id.buttonSignin);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewSignup = (TextView)findViewById(R.id.textViewSignup);

        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String name = user.getDisplayName();
                        String email = user.getEmail();
                        Uri imageUri = user.getPhotoUrl();



                        //Profile.getCurrentProfile().getLinkUri().toString()
                        // 1 -> is from facebook login
                        // 0 -> from regular login
                        User myUser = new User(name, "", email,1,"");
                        myUser.setFacebookLink(Profile.getCurrentProfile().getLinkUri().toString());
                        myUser.setUriImage(String.valueOf(imageUri));




                        firebaseHelper.save(user,myUser);

                        startActivity(new Intent(getApplicationContext(), ContainerActivity.class));

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signOut() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignin){
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.buttonclick));
            userLogin();
        }
        
        if (v == textViewSignup){
            //
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)){
            //email field is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password field is empty
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Login user...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){
                    //start the profile activity

                    startActivity(new Intent(getApplicationContext(),ContainerActivity.class));
                }
                else
                {

                }

            }
        });



    }
}
