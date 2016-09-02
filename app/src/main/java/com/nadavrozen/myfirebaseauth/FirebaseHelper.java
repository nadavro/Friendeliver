package com.nadavrozen.myfirebaseauth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Israel Rozen on 02/09/2016.
 */
public class FirebaseHelper {
    DatabaseReference db;
    boolean saved;

    public FirebaseHelper(DatabaseReference db){
        this.db = db;
    }

    public boolean save(FirebaseUser firebaseUser, User user){
        if(user == null){
            saved = false;
        }
        else{
            try {
                db.child("User").child(firebaseUser.getUid()).setValue(user);
                saved = true;
            }
            catch (DatabaseException e){
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }

}
