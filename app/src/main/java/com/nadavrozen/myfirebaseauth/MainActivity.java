package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.Signature;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private class NumericKeyBoardTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return source;
        }
    }

    private Button buttonRegister;
    private EditText firstName,lastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText year;
    private EditText day;
    private EditText month;
    private TextView textViewSignin;
    private EditText birthday;
    private ProgressDialog progressDialog;
    private DatabaseReference db;
    FirebaseHelper firebaseHelper;
    private FirebaseAuth firebaseAuth;
    private static final String FIREBASE_URL = "https://myfirstfirebaseauth.firebaseio.com";
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.nadavrozen.myfirebaseauth",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        db= FirebaseDatabase.getInstance().getReference();
        firebaseHelper=new FirebaseHelper(db);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ContainerActivity.class));

        }

        progressDialog = new ProgressDialog(this);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        birthday = (EditText)findViewById(R.id.dobEditText);
        MyEditTextDatePicker dp = new MyEditTextDatePicker(this,birthday.getId());
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewSignin = (TextView)findViewById(R.id.textViewSignin);


        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == buttonRegister){
            registerUser();

        }
        if (v == textViewSignin){
            //will open login activity
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    private void registerUser() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String first = firstName.getText().toString().trim();
        final String last = lastName.getText().toString().trim();
        final String dob = birthday.getText().toString();
        if(TextUtils.isEmpty(first)){
            //first name is empty
            Toast.makeText(this,"Please enter First name",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(last)){
            //first name is empty
            Toast.makeText(this,"Please enter Last name",Toast.LENGTH_SHORT).show();
            return;

        }
        if(TextUtils.isEmpty(dob)){
            //first name is empty
            Toast.makeText(this,"Please enter birthday",Toast.LENGTH_SHORT).show();
            return;

        }


        if (TextUtils.isEmpty(email)){
            //email field is empty
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password field is empty
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                progressDialog.hide();
                if (task.isSuccessful()) {
                    WriteNewUser(task.getResult().getUser(), first + " " + last, dob, email);
//                    Firebase ref = new Firebase(FIREBASE_URL);
//                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
//                        @Override
//                        public void onAuthenticated(AuthData authData) {
//                            System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
//                        }
//                        @Override
//                        public void onAuthenticationError(FirebaseError firebaseError) {
//                            // there was an error
//                        }
//                    });
                    //firebaseHelper.save(new User(first,dob,email));
                    //finally, the user is registered!
                    finish();
                    startActivity(new Intent(getApplicationContext(), ContainerActivity.class));
                    //Toast.makeText(MainActivity.this, "Congratulations! you have been registered successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Register failed", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void WriteNewUser(FirebaseUser user, String name, String dob, String email) {
        User myUser = new User(name,dob,email);
        firebaseHelper.save(user,myUser);

    }
}
