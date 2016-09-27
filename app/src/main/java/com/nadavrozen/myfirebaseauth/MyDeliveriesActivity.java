package com.nadavrozen.myfirebaseauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
    private MyDeliveriesAdapter adapter;
    private String lookForUserKey;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_my_deliveries, container, false);

        delsListView = (ListView)view.findViewById(R.id.delsListView);
        myDeliveries = new ArrayList<LookForUser>();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String key = firebaseAuth.getCurrentUser().getUid();
        Query myTopPostsQuery = mDatabase.child("LookForUser").
                orderByChild("uid")
                .equalTo(key);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<LookForUser> l = new ArrayList<LookForUser>();
                for (DataSnapshot usi : dataSnapshot.getChildren()) {
                    LookForUser lookForUser = usi.getValue(LookForUser.class);
                    lookForUser.setKey(usi.getKey());

                    if (lookForUser.getDelivery().getStatus() != "CANCEL") {
                        l.add(lookForUser);
                    }

                }
                setDeliveriesList(l);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }


    private void setDeliveriesList(ArrayList<LookForUser> l)
    {



        this.myDeliveries = l;
        MyDeliveriesAdapter adapter = new MyDeliveriesAdapter(this.getActivity().getWindow().getContext()
                ,R.layout.listview_row_dels, myDeliveries);
        this.adapter = adapter;
        delsListView.setAdapter(adapter);
        delsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View arg1,
                                           int position, long id) {

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.my_deliveries_dialog, null);
                LookForUser current = (LookForUser) adapter.getItemAtPosition(position);
                String deluid = current.getDelivery().getDeliverUserUid();




                TextView info = (TextView)alertLayout.findViewById(R.id.contenTxtView);

                TextView from = (TextView)alertLayout.findViewById(R.id.fromTxtView);
                TextView to = (TextView)alertLayout.findViewById(R.id.toTxtView);
                TextView status = (TextView)alertLayout.findViewById(R.id.statusTxtView);
//


                info.setText(current.getDelivery().getDesc());

                from.setText(current.getDelivery().getStrOrigin());
                to.setText(current.getDelivery().getStrDest());
                status.setText(current.getDelivery().getStatus());
                if (current.getDelivery().getStatus().equals("PENDING")){
                    status.setTextColor(Color.parseColor("red"));
                }

                else
                {


                    status.setTextColor(Color.parseColor("green"));

                    cont(deluid, alertLayout, position, current); // accepted deliveries
//

                }



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


                AlertDialog dialog = alert.create();
                dialog.show();





                return true;
            }
        });

    }




    private void cont(String current, final View alertLayout, final int position, final LookForUser lookForUser) {

        DatabaseReference refi = mDatabase.child("DeliverUser").
                child(current);
        refi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DeliverUser usi = dataSnapshot.getValue(DeliverUser.class);
                setCurrentDeliverUser(usi,alertLayout,position,lookForUser);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setCurrentDeliverUser(final DeliverUser usi, View alertLayout, final int position, final LookForUser lookForUser) {
        this.currDeliverUser = usi;
        Button finishButton = (Button)alertLayout.findViewById(R.id.finishBtn);
        TextView deliverName = (TextView)alertLayout.findViewById(R.id.delNameTxtView);
        TextView deliveNameField = (TextView)alertLayout.findViewById(R.id.delNameFieldTxtView);
        TextView deliverDateField = (TextView)alertLayout.findViewById(R.id.delDateFieldTxtView);
        TextView deliverDate = (TextView)alertLayout.findViewById(R.id.delDateldTxtView);
        //finish deliver with review?

        finishButton.setVisibility(View.VISIBLE);

        deliveNameField.setText("By:");
        deliverName.setText(usi.getUser().getFullName());
        deliverName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment fragment = new UserProfile();
                Fragment fragment = new UserProfile();
                final Bundle bundle = new Bundle();
                bundle.putParcelable("User",usi.getUser());
                bundle.putString("userKey",usi.getUid());

                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().
                        setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });
        deliverDateField.setText("On");
        deliverDate.setText(usi.getDateStr() +" at "+usi.getArriveAtStr());
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.review_dialog_custom, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                dialogBuilder.setTitle("One more thing..");
                dialogBuilder.setMessage("Please write a review about " + usi.getUser().getFullName());
                dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //do something with edt.getText().toString();
                        Review review = new Review(myDeliveries.get(0).getUser().getFullName()
                                , edt.getText().toString(), usi.getUid());
                        mDatabase.child("Review").push().setValue(review);
                        //remove from delivery
                        //DatabaseReference s = mDatabase.child("Delivery").child(lookForUser.getDelivery().getKey());
                        //s.removeValue();

                        DatabaseReference q = mDatabase.child("LookForUser").child(lookForUser.getKey());
                        q.removeValue();
                        DatabaseReference t = mDatabase.child("Duty").child(lookForUser.getDutyId());
                        t.removeValue();
                        adapter.remove(adapter.getItem(position));


                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }


        });
    }

}
