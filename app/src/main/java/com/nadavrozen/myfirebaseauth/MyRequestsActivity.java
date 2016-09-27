package com.nadavrozen.myfirebaseauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The requests that a user receive for picking-up things for other people
 */
public class MyRequestsActivity extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private ArrayList<DeliverUser> list = new ArrayList<DeliverUser>();
    private ArrayList<Request> finalArray = new ArrayList<Request>();
    private User user;
    private ListView listView;
    private ArrayList<Request> reqList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_requests, container, false);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_requests);
        listView = (ListView) view.findViewById(R.id.ListViewReq);


        //get who am i
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        if (firebaseAuth.getCurrentUser() == null) {
//            finish();
//            startActivity(new Intent(this, ProfileActivity.class));
//        }
        //System.out.println("dsdsdsdsdjsxxnxnxnxnxnxnxnxnxnxnxnnxxnxnx");
        String key = firebaseAuth.getCurrentUser().getUid();
        Query query = mDatabase.child("Request").orderByChild("delUserUID").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Request> tempReqList = new ArrayList<Request>();
                for (DataSnapshot usi : dataSnapshot.getChildren()){
                    Request req = usi.getValue(Request.class);
                    req.setKey(usi.getKey());
                   // System.out.println(req.getStatus());
                    if (req.getStatus().equals("WAITING")){
                        tempReqList.add(req);
                    }
                    //tempReqList.add(req);

                }
                setReqList(tempReqList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    public void setReqList(ArrayList<Request> reqList) {
        this.reqList = reqList;
        MyRequestsAdapter adapter = new MyRequestsAdapter(getActivity().getApplicationContext(),
                R.layout.list_view_row_req, reqList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.my_request_dialog, null);
                final Request current = (Request) parent.getItemAtPosition(position);

                TextView what = (TextView) alertLayout.findViewById(R.id.what);
                TextView specAddressDepart =
                        (TextView) alertLayout.findViewById(R.id.specAddressDepart);
                TextView specAddressArrive =
                        (TextView) alertLayout.findViewById(R.id.specAddressArive);
                Button showProfileButton = (Button) alertLayout.findViewById(R.id.showProfiled);


                what.setText(current.getLookForUser().getDelivery().getDesc());
                specAddressDepart.setText(current.getLookForUser().getDelivery().getStrOrigin());
                specAddressArrive.setText(current.getLookForUser().getDelivery().getStrDest());
                showProfileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new UserProfile();
                        final Bundle bundle = new Bundle();
                        bundle.putParcelable("User",current.getLookForUser().getUser());
                        bundle.putString("userKey",current.getLookForUser().getUid());

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().
                                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                    }
                });

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                int pos = position + 1;
                alert.setTitle("Request #" + pos);
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

//        Query query = mDatabase.child("DeliverUser").orderByChild("uid").equalTo(key);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//
//            //take all the deliveruser with this id in order to pull the requests list
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ArrayList<DeliverUser> l = new ArrayList<DeliverUser>();
//                for (DataSnapshot usi: dataSnapshot.getChildren()){
//                    DeliverUser deliverUser = usi.getValue(DeliverUser.class);
//
//                    l.add(deliverUser);
//                }
//                setList(l);
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
//
//    public void setList(ArrayList<DeliverUser> list) {
//        this.list = list;
//        for (DeliverUser current : list){
//            for (Request req : current.getRequests()){
//                this.finalArray.add(req);
//            }
//        }
//        MyRequestsAdapter adapter = new MyRequestsAdapter(this,R.layout.list_view_row_req, finalArray);
//        listView.setAdapter(adapter);
//
//    }
}
