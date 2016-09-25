package com.nadavrozen.myfirebaseauth;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends Fragment {
    ListView listView;
    private DatabaseReference mDatabase;
    public ArrayList<DeliverUser> deliverList;
    private Delivery delivery;
    private String delkey;
    private LookForUser lookForUser;
    private String lookForUserID;
    private LookForUser lookUser;
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_result, container, false);
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_result);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        listView = (ListView)view.findViewById(R.id.myListView);

        System.out.println("in resultActivity");

        //Getting the delivery object from the other activity
        Bundle extras = this.getArguments();
        //delkey = null;
        delivery = null;
        lookForUserID = null;
        if (extras != null) {
            delivery = extras.getParcelable("deliveryObj");
            //lookForUser = extras.getParcelable("lookForUser");
            lookForUserID = extras.getString("lookForUserID");
            //delkey = (String) extras.get("deliveryUid");
            //Toast.makeText(ResultActivity.this,delivery.getCityArrive(),Toast.LENGTH_SHORT).show();

        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Searching");
        progressDialog.show();
        //System.out.println("hahahahahah "+delivery.getKey());
        DatabaseReference q = mDatabase.child("LookForUser").child(lookForUserID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lookUser = dataSnapshot.getValue(LookForUser.class);
                lookUser.setKey(dataSnapshot.getKey());
                //lookUser.getDelivery().setKey(delivery.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //finding all delivery guys who arrived to the destination city of the package
        Query myTopPostsQuery = mDatabase.child("DeliverUser").orderByChild("cityArrive").
                equalTo(delivery.getCityArrive());
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<DeliverUser> l = new ArrayList<DeliverUser>();
                for (DataSnapshot usi : dataSnapshot.getChildren()) {
                    DeliverUser del = usi.getValue(DeliverUser.class);
                    del.setKey(usi.getKey());
                    //System.out.println("in result ");
                    l.add(del);

                }
                setDeliverList(l);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }

    public void setDeliverList(ArrayList<DeliverUser> deliverList) {
        this.deliverList = deliverList;
        progressDialog.dismiss();
        if (deliverList.size()==0){
            DatabaseReference yt = mDatabase.child("LookForUser").child(lookForUserID);
            yt.removeValue();
            new AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle)
                    .setTitle("NO MATCHES FOUND")
                    .setMessage("Unfortunately, there is currently no one who can pick up your things. Try again later!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Fragment fragment =new  ProfileActivity();
                            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                                    .setCustomAnimations(android.R.anim.slide_in_left,
                                            android.R.anim.slide_out_right).addToBackStack(null).commit();

                            // continue with delete
                        }
                    })

                    .show();
        }
        //System.out.println(deliverList.size());
        cont();



    }

    private void cont() {
        DeliverUser[] deli = new DeliverUser[deliverList.size()];
        deli = deliverList.toArray(deli);
        //lookUser.getDelivery().setKey(delivery.getKey());

        DeliverAdapter adapter = new DeliverAdapter(getActivity(),R.layout.listview_item_row, deli,delivery,lookUser);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.result_dialog, null);
                final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(getActivity(),R.style.MyAlertDialogStyle);
                final DeliverUser currentDeliver = (DeliverUser) adapter.getItemAtPosition(position);
                TextView date = (TextView) alertLayout.findViewById(R.id.dateDeliver);
                TextView specAddressDepart =
                        (TextView) alertLayout.findViewById(R.id.from);
                TextView specAddressArrive =
                        (TextView) alertLayout.findViewById(R.id.to);
                Button showProfileButton = (Button) alertLayout.findViewById(R.id.showProfile);

                date.setText(currentDeliver.getDateStr());
                specAddressDepart.setText(currentDeliver.getDepartStr() + " at "+currentDeliver.getDepartAtStr());
                specAddressArrive.setText(currentDeliver.getArriveStr()+ " at "+currentDeliver.getArriveAtStr());

                showProfileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Showing the user profile screen
                        Fragment fragment = new UserProfile();
                        final Bundle bundle = new Bundle();
                        bundle.putParcelable("User",currentDeliver.getUser());
                        bundle.putString("userKey",currentDeliver.getUid());

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().
                                setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();


                    }
                });

                int pos = position + 1;
                //alert.setTitle("Request #" + pos);
                alert.setView(alertLayout);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //dosomething();

                android.app.AlertDialog dialog = alert.create();
                dialog.show();


//
//

            }
        });
    }
}
