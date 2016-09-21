package com.nadavrozen.myfirebaseauth;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * All of the deliveries that the user need to pick-up for other people
 */
public class MyDutiesActivity extends Fragment {


    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ArrayList<Duty> duties;
    private ListView listView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_duties, container, false);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_duties);

        this.listView = (ListView)view.findViewById(R.id.dutyListView);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        if (firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,ProfileActivity.class));
//        }
        String key = firebaseAuth.getCurrentUser().getUid();

        //ArrayList<Duty> dutyArrayList = new ArrayList<Duty>();
        Query query = mDatabase.child("Duty").orderByChild("uid").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Duty> dutyArrayList = new ArrayList<Duty>();
                for (DataSnapshot usi : dataSnapshot.getChildren()){
                    Duty duty = usi.getValue(Duty.class);
                    dutyArrayList.add(duty);
                }
                setDuties(dutyArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    public void setDuties(ArrayList<Duty> duties) {
        this.duties = duties;
        MyDutiesAdapter adapter = new MyDutiesAdapter(getActivity(),R.layout.list_view_row_duty, this.duties);
        listView.setAdapter(adapter);

    }
}
