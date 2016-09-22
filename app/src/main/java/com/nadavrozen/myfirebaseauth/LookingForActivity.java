package com.nadavrozen.myfirebaseauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * The Looking-for-a-deliver activity
 */
public class LookingForActivity extends Fragment implements View.OnClickListener {
    String url;
    private static final String TAG_RESULT = "predictions";
    JSONObject json;
    String browserKey = "AIzaSyBeUg81xPA5e8XUqjoAHcoEPLe3bpYSprg";


    AutoCompleteTextView auto_tvOrigin ;
    ArrayList<String> names;
    ArrayList<String> namesDest;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    AutoCompleteTextView auto_tvDest;

    private EditText shortDesc;
    private Button findButton;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    private ArrayList<DeliverUser> ans;
    private User me;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_looking_for, container, false);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_looking_for);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,ProfileActivity.class));
//        }

        user = firebaseAuth.getCurrentUser();
        String key = firebaseAuth.getCurrentUser().getUid();

        mDatabase.child("User").child(key).addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                me = dataSnapshot.getValue(User.class);
                //String s = userName.substring(0,userName.lastIndexOf(" "));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        auto_tvOrigin = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView1);
        auto_tvDest = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView2);
        shortDesc = (EditText)view.findViewById(R.id.descriptionEditText);
        findButton = (Button)view.findViewById(R.id.findButton);
        auto_tvOrigin.setThreshold(0);
        auto_tvDest.setThreshold(0);
        names = new ArrayList<String>();
        namesDest = new ArrayList<String>();

        auto_tvOrigin .addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length() <= 30) {
                    names = new ArrayList<String>();
                    updateList(s.toString());
                }

            }
        });

        auto_tvDest.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length() <= 30) {
                    namesDest = new ArrayList<String>();
                    updateList2(s.toString());
                }

            }
        });



        //Now what?!
        findButton.setOnClickListener(this);

        return view;

    }

    public void updateList2(String place) {
        String input = "";
        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String output = "json";
        String parameter = input + "&types=geocode&sensor=true&key="
                + browserKey;

        url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                + output + "?" + parameter;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                   // Log.v("HERE",response.toString());
                    JSONArray ja = response.getJSONArray(TAG_RESULT);

                    for (int i = 0; i < ja.length(); i++) {
                        //--???----
                        JSONObject c = ja.getJSONObject(i);
                        String description = c.getString("description");
                       // Log.d("description", description);
                        namesDest.add(description);
                    }

                    adapter2 = new ArrayAdapter<String>(
                            getActivity().getApplicationContext(),
                            android.R.layout.simple_list_item_1, namesDest) {
                        @Override
                        public View getView(int position,
                                            View convertView, ViewGroup parent) {
                            View view = super.getView(position,
                                    convertView, parent);
                            TextView text = (TextView) view
                                    .findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    auto_tvDest.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplication.getInstance().addToReqQueue(jsonObjReq, "jreq");
    }


    public void updateList(String place) {
        String input = "";

        try {
            input = "input=" + URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        String output = "json";
        String parameter = input + "&types=geocode&sensor=true&key="
                + browserKey;

        url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
                + output + "?" + parameter;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    //Log.v("HERE",response.toString());
                    JSONArray ja = response.getJSONArray(TAG_RESULT);

                    for (int i = 0; i < ja.length(); i++) {
                        //--???----
                        JSONObject c = ja.getJSONObject(i);
                        String description = c.getString("description");
                       // Log.d("description", description);
                        names.add(description);
                    }

                    adapter = new ArrayAdapter<String>(
                           getActivity().getApplicationContext(),
                            android.R.layout.simple_list_item_1, names) {
                        @Override
                        public View getView(int position,
                                            View convertView, ViewGroup parent) {
                            View view = super.getView(position,
                                    convertView, parent);
                            TextView text = (TextView) view
                                    .findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    auto_tvOrigin.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        MyApplication.getInstance().addToReqQueue(jsonObjReq, "jreq");
    }

    @Override
    public void onClick(View v) {
        if (v == findButton){
            String strOrigin = auto_tvOrigin.getText().toString();
            String strDest = auto_tvDest.getText().toString();
            String desc = shortDesc.getText().toString();

            if (strOrigin.length()==0){
                Toast.makeText(getActivity(),
                        "please select an origin for the delivery",Toast.LENGTH_SHORT).show();
                return;
            }
            if (strDest.length()==0){
                Toast.makeText(getActivity(),
                        "please select a destination for the delivery",Toast.LENGTH_SHORT).show();
                return;
            }
            if (desc.length()==0){
                Toast.makeText(getActivity(),
                        "Please specify what to you want to deliver",Toast.LENGTH_SHORT).show();
                return;
            }
            //writing to database
            writeNewDelivery(strOrigin, strDest, desc);




        }
    }




    /**
     * Writing new Delivery object to the firebase database
     * @param strOrigin
     * @param strDest
     * @param desc
     */
    private void writeNewDelivery(String strOrigin, String strDest, String desc) {
        Delivery delivery = new Delivery(strOrigin,strDest,desc,user.getUid());
        LookForUser lookForUser = new LookForUser(me,delivery,user.getUid());

        DatabaseReference d = mDatabase.child("Delivery").push();
        String delKey = d.getKey();
        d.setValue(delivery);
        DatabaseReference ref = mDatabase.child("LookForUser").push();

        String lookKey = ref.getKey();
        delivery.setKey(delKey);
        //System.out.println(delKey);

        ref.setValue(lookForUser);
        //ans = LookForUser.FindMatches(delivery,mDatabase);
        Fragment fragment = new ResultActivity();

        final Bundle bundle = new Bundle();
        bundle.putParcelable("deliveryObj",delivery);
        bundle.putString("lookForUserID",lookKey);
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction().
                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
//        Intent intent = new Intent(this,ResultActivity.class);
//        //Now, call the activity that show result
//        intent.putExtra("deliveryObj",delivery);
//        intent.putExtra("lookForUserID",lookKey);
//        //intent.putExtra("deliveryUid",delKey);
//        startActivity(intent);



    }
}