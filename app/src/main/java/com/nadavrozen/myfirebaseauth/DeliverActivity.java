package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The want-to-be-a-deliver activity
 */
public class DeliverActivity extends Fragment implements View.OnClickListener {
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
    private Button postButton;
    private DatabaseReference mDatabase;
    User me;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_deliver, container, false);

//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_deliver);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        if (firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,ProfileActivity.class));
//        }
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

        postButton = (Button)view.findViewById(R.id.findButton);
        user = firebaseAuth.getCurrentUser();
        depart = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextView1);
        arrive = (AutoCompleteTextView)view.findViewById(R.id.autoCompleteTextView2);

        date = (EditText)view.findViewById(R.id.dateEditText);
        date.setInputType(InputType.TYPE_NULL);
        date.setFocusable(false);
        departAt = (EditText)view.findViewById(R.id.departEditText);
        departAt.setInputType(InputType.TYPE_NULL);
        departAt.setFocusable(false);
        arrivesAt = (EditText)view.findViewById(R.id.arrivesEditText);
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
        postButton.setOnClickListener(this);

        return view;
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
            mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                    selectedmonth = selectedmonth + 1;
                    date.setText("" + selectedday + "/" + selectedmonth + "/" + selectedyear);
                }
            }, mYear, mMonth, mDay);
            mDatePicker.setTitle("Select Date");
            mDatePicker.getDatePicker().setCalendarViewShown(false);

            mDatePicker.show();

        }
        if (v == departAt){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
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
            mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    arrivesAt.setText( selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();


        }
        if(v == postButton){
            String dateStr = date.getText().toString();
            String arriveStr = arrive.getText().toString();
            String departStr = depart.getText().toString();
            String arriveAtStr = arrivesAt.getText().toString();
            String departAtStr = departAt.getText().toString();

            if (TextUtils.isEmpty(dateStr)){
                //date is empty

            }
            if(TextUtils.isEmpty(arriveStr)){

            }
            if(TextUtils.isEmpty(departStr)){

            }
            if(TextUtils.isEmpty(arriveAtStr)){

            }
            if(TextUtils.isEmpty(departAtStr)){

            }
            //Now, we can add new Deliver to the database
            writeNewDeliverUser(dateStr,arriveStr,departStr,
                    arriveAtStr,departAtStr,user.getUid());

            Fragment fragment = new ProfileActivity();
            getFragmentManager().beginTransaction().
                    setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        }
    }

    /**
     * Creating and writing new Deliver to the database
     * @param dateStr - the date the deliver depart
     * @param arriveStr - when he arrive
     * @param departStr - when he depart
     * @param arriveAtStr - in what time he exactly arrive
     * @param departAtStr - in what tine he exactly depart
     * @param uid - the firebase userID
     */
    private void writeNewDeliverUser(String dateStr, String arriveStr, String departStr,
                                     String arriveAtStr, String departAtStr, String uid) {

        //System.out.println(me.getFullName());
        DeliverUser deliverUser = new DeliverUser(me,dateStr,arriveStr,departStr,arriveAtStr,
                departAtStr,uid);
       // System.out.println("in deliveruser"+deliverUser.getKey());
        //System.out.println(deliverUser.getFullName());
        DatabaseReference d =   mDatabase.child("DeliverUser").push();
        String delKey = d.getKey();
        deliverUser.setKey(delKey);
        System.out.println("tititititittititi" + deliverUser.getKey());
        d.setValue(deliverUser);

        new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle)
                .setTitle("Thank you!").setMessage("Now, Other people can ask you for picking-up their" +
                " things. Have a safe drive!").setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();




    }


}
