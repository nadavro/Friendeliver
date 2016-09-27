package com.nadavrozen.myfirebaseauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
                    duty.setKey(usi.getKey());
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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_my_duties, null);
                final Duty current = (Duty) parent.getItemAtPosition(position);

                TextView what = (TextView)alertLayout.findViewById(R.id.what);
                TextView specAddressDepart=
                        (TextView)alertLayout.findViewById(R.id.specAddressDepart);
                TextView specAddressArrive =
                        (TextView)alertLayout.findViewById(R.id.specAddressArive);
                final TextView phone = (TextView)alertLayout.findViewById(R.id.phone);

                what.setText(current.getLookForUser().getDelivery().getDesc());
                specAddressDepart.setText(current.getLookForUser().getDelivery().getStrOrigin());
                specAddressArrive.setText(current.getLookForUser().getDelivery().getStrDest());
                phone.setText(current.getLookForUser().getUser().getPhone());
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + current.getLookForUser().getUser().getPhone()));
                        startActivity(intent);
                    }
                });
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                int pos = position+1;
                alert.setTitle("Delivery #" + pos);
                alert.setView(alertLayout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //dosomething();

                AlertDialog dialog = alert.create();
                dialog.show();
                return true;
            }
        });

    }
}
