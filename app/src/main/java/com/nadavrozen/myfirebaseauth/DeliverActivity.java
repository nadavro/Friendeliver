package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The want-to-be-a-deliver activity
 */
public class DeliverActivity extends AppCompatActivity implements View.OnClickListener {
    String url;
    private static final String TAG_RESULT = "predictions";
    JSONObject json;
    String browserKey = "AIzaSyBeUg81xPA5e8XUqjoAHcoEPLe3bpYSprg";

    private AutoCompleteTextView depart;
    private AutoCompleteTextView arrive;
    private EditText date;
    private EditText departAt;
    private EditText arrivesAt;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private ArrayList<String> names;
    private ArrayList<String> namesDest;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }
        user = firebaseAuth.getCurrentUser();
        depart = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        arrive = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView2);

        date = (EditText)findViewById(R.id.dateEditText);
        date.setInputType(InputType.TYPE_NULL);
        date.setFocusable(false);
        departAt = (EditText)findViewById(R.id.departEditText);
        departAt.setInputType(InputType.TYPE_NULL);
        departAt.setFocusable(false);
        arrivesAt = (EditText)findViewById(R.id.arrivesEditText);
        arrivesAt.setInputType(InputType.TYPE_NULL);
        arrivesAt.setFocusable(false);

        depart.setThreshold(0);
        arrive.setThreshold(0);
        names = new ArrayList<String>();
        namesDest = new ArrayList<String>();

        depart .addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length() <= 30) {
                    names = new ArrayList<String>();
                    updateList(s.toString(),1);
                }

            }
        });
        arrive .addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (s.toString().length() <= 30) {
                    names = new ArrayList<String>();
                    updateList(s.toString(),2);
                }

            }
        });


        date.setOnClickListener(this);
        departAt.setOnClickListener(this);
        arrivesAt.setOnClickListener(this);

    }
    public void updateList(String place, final int i) {
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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    Log.v("HERE",response.toString());
                    JSONArray ja = response.getJSONArray(TAG_RESULT);

                    for (int i = 0; i < ja.length(); i++) {
                        //--???----
                        JSONObject c = ja.getJSONObject(i);
                        String description = c.getString("description");
                        Log.d("description", description);
                        names.add(description);
                    }

                    adapter = new ArrayAdapter<String>(
                            getApplicationContext(),
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
                    if (i==1){
                        depart.setAdapter(adapter);
                    }
                    else{
                        arrive.setAdapter(adapter);
                    }
                   // depart.setAdapter(adapter);
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

        if(v == date){
            Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker;
            mDatePicker = new DatePickerDialog(DeliverActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedmonth = selectedmonth + 1;
                    date.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                }
            }, mYear, mMonth, mDay);
            mDatePicker.setTitle("Select Date");
            mDatePicker.show();

        }
        if (v == departAt){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(DeliverActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    departAt.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
        if (v == arrivesAt){ //maybe think about efficiency
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(DeliverActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    arrivesAt.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }

    }
}
