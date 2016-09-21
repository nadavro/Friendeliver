package com.nadavrozen.myfirebaseauth;

import com.firebase.client.FirebaseError;

/**
 * Created by Israel Rozen on 20/09/2016.
 */
public interface OnDataLoadedListener {
    public void onFinishLoading(DeliverUser data);
    public void onCancelled(FirebaseError firebaseError);
}
