package com.nadavrozen.myfirebaseauth;

/**
 * Created by Israel Rozen on 25/09/2016.
 */


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class SendNotification {
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String SERVER_KEY = "AIzaSyBAq3JEwjgBCTXT7Yxx5lUo3ZwnN6EZzl4";
    OkHttpClient mClient = new OkHttpClient();

    public void sendMessage(final String fcmToken, final String title, final String body, final String icon, final String message) {
        final JSONArray recipients;
        try {
            String[] arr = {fcmToken};
            recipients = new JSONArray(arr);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);
                    notification.put("sound", "default");

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    System.out.println("notification result:" +result);
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                   // System.out.println( "Message Success: " + success + "Message Failed: " + failure);
                } catch (JSONException e) {
                    e.printStackTrace();
                   // System.out.println( "Message Failed, Unknown error occurred.");
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + SERVER_KEY)
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
