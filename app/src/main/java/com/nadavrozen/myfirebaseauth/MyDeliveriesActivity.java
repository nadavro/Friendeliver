package com.nadavrozen.myfirebaseauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * All of my request for deliveries
 */
public class MyDeliveriesActivity extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ArrayList<LookForUser> myDeliveries;
    private ListView delsListView;
    private DeliverUser currDeliverUser;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_deliveries, container, false);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_deliveries);
        delsListView = (ListView)view.findViewById(R.id.delsListView);
        myDeliveries = new ArrayList<LookForUser>();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        if (firebaseAuth.getCurrentUser() == null){
//            finish();
//            startActivity(new Intent(this,ProfileActivity.class));
//        }
        String key = firebaseAuth.getCurrentUser().getUid();
        Query myTopPostsQuery = mDatabase.child("LookForUser").
                orderByChild("uid")
                .equalTo(key);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<LookForUser> l = new ArrayList<LookForUser>();
                for (DataSnapshot usi : dataSnapshot.getChildren()){
                    LookForUser lookForUser = usi.getValue(LookForUser.class);
                    l.add(lookForUser);
                }
                setDeliveriesList(l);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

//                equalTo(key);
//        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final ArrayList<Delivery> l = new ArrayList<Delivery>();
//                for (DataSnapshot usi : dataSnapshot.getChildren()) {
//                    Delivery del = usi.getValue(Delivery.class);
//                    //System.out.println("in result ");
//                    l.add(del);
//
//                }
//                setDeliveriesList(l);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
//
//    }
//
    private void setDeliveriesList(ArrayList<LookForUser> l)
    {



        this.myDeliveries = l;
        MyDeliveriesAdapter adapter = new MyDeliveriesAdapter(getActivity().getApplicationContext()
                ,R.layout.listview_row_dels, myDeliveries);
        delsListView.setAdapter(adapter);
        delsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View arg1,
                                           int position, long id) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.my_deliveries_dialog, null);
                LookForUser current = (LookForUser) adapter.getItemAtPosition(position);
                String deluid = current.getDelivery().getDeliverUserUid();

                //dosomething();
                TextView info = (TextView)alertLayout.findViewById(R.id.contenTxtView);
                //TextView date = (TextView)alertLayout.findViewById(R.id.dateTxtView);
                TextView from = (TextView)alertLayout.findViewById(R.id.fromTxtView);
                TextView to = (TextView)alertLayout.findViewById(R.id.toTxtView);
                TextView status = (TextView)alertLayout.findViewById(R.id.statusTxtView);
//                TextView deliverName = (TextView)alertLayout.findViewById(R.id.delNameTxtView);
//                TextView deliveNameField = (TextView)alertLayout.findViewById(R.id.delNameFieldTxtView);


                info.setText(current.getDelivery().getDesc());
                // date.setText(current.);
                from.setText(current.getDelivery().getStrOrigin());
                to.setText(current.getDelivery().getStrDest());
                status.setText(current.getDelivery().getStatus());
                if (current.getDelivery().getStatus().equals("PENDING")){
                    status.setTextColor(Color.parseColor("red"));
                }
                else{


                    System.out.println("lllllllllllllllllllllllllllllllll");
                    status.setTextColor(Color.parseColor("green"));
                    cont(deluid, alertLayout);
//                    deliveNameField.setText("Deliver By:");
//                    deliverName.setText(currDeliverUser.getUser().getFullName());

                }
                //else{
                 //   status.setTextColor(Color.parseColor("green"));
                //}



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

    private void dosomething() {
    }


    private void cont(String current, final View alertLayout) {
       System.out.println("in AAAAAAAAAAAAAAA "+current);
        DatabaseReference refi = mDatabase.child("DeliverUser").
                child(current);
        refi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                DeliverUser usi = dataSnapshot.getValue(DeliverUser.class);
                setCurrentDeliverUser(usi,alertLayout);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setCurrentDeliverUser(DeliverUser usi, View alertLayout) {
        this.currDeliverUser = usi;
        TextView deliverName = (TextView)alertLayout.findViewById(R.id.delNameTxtView);
        TextView deliveNameField = (TextView)alertLayout.findViewById(R.id.delNameFieldTxtView);
        TextView deliverDateField = (TextView)alertLayout.findViewById(R.id.delDateFieldTxtView);
        TextView deliverDate = (TextView)alertLayout.findViewById(R.id.delDateldTxtView);

        deliveNameField.setText("By:");
        deliverName.setText(usi.getUser().getFullName());
        deliverDateField.setText("On");
        deliverDate.setText(usi.getDateStr() +" at "+usi.getArriveAtStr());



    }
//
//    public ArrayList<Delivery> getMyDeliveries() {
//        return myDeliveries;
//    }


//    public void setMyDeliveries(ArrayList<LookForUser> myDeliveriesii) {
//        this.myDeliveries = myDeliveriesii;
//        System.out.println(myDeliveries.size());
//        MyDeliveriesAdapter adapter = new MyDeliveriesAdapter(this,R.layout.listview_row_dels, myDeliveries);
//        delsListView.setAdapter(adapter);
//                delsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapter, View arg1,
//                                           int position, long id) {
//                LayoutInflater inflater = getLayoutInflater();
//                View alertLayout = inflater.inflate(R.layout.my_deliveries_dialog, null);
//                TextView info = (TextView)alertLayout.findViewById(R.id.contenTxtView);
//                //TextView date = (TextView)alertLayout.findViewById(R.id.dateTxtView);
//                TextView from = (TextView)alertLayout.findViewById(R.id.fromTxtView);
//                TextView to = (TextView)alertLayout.findViewById(R.id.toTxtView);
//                TextView status = (TextView)alertLayout.findViewById(R.id.statusTxtView);
//
//                LookForUser current = (LookForUser) adapter.getItemAtPosition(position);
//                info.setText(current.getDelivery().getDesc());
//                // date.setText(current.);
//                from.setText(current.getDelivery().getStrOrigin());
//                to.setText(current.getDelivery().getStrDest());
//                status.setText(current.getDelivery().getStatus());
//                if (current.getDelivery().getStatus().equals("PENDING")){
//                    status.setTextColor(Color.parseColor("red"));
//                }
//                //else{
//                 //   status.setTextColor(Color.parseColor("green"));
//                //}
//
//
//
//                final AlertDialog.Builder alert = new AlertDialog.Builder(MyDeliveriesActivity.this);
//                int pos = position+1;
//                alert.setTitle("Delivery #" + pos);
//                alert.setView(alertLayout);
//                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog dialog = alert.create();
//                dialog.show();
//
//
//
//
//
//                return true;
//            }
//        });
//    }
}
