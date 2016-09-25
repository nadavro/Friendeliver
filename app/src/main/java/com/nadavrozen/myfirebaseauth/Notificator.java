package com.nadavrozen.myfirebaseauth;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Israel Rozen on 25/09/2016.
 */
public class Notificator {

    public static void sendNotificationToUser(String user, final String message) {
        Firebase ref = new Firebase("https://myfirstfirebaseauth.firebaseio.com/");
        final Firebase notifications = ref.child("notificationRequests");

        Map notification = new HashMap<>();
        notification.put("username", user);
        notification.put("message", message);

        notifications.push().setValue(notification);
    }
}
