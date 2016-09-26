package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Israel Rozen on 19/09/2016.
 */
public class MyRequestsAdapter extends ArrayAdapter<Request> {
    private  Context context;
    private  int layoutResourceId;
    private  List<Request> reqList;
    DatabaseReference mDataBase;
    private LookForUser lookForUser;
    private LookForUser holderWhat;
    private DeliveryHolder holder;
    private DeliverUser delUser;


    public MyRequestsAdapter(Context context, int layoutResourceId, List<Request> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.reqList = objects;
        mDataBase =  FirebaseDatabase.getInstance().getReference();

    }

    public View getView (final int position, View convertView, ViewGroup parent){
        cont(position);

        View row = convertView;
        holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeliveryHolder();
            holder.who = (TextView)row.findViewById(R.id.txtTitle);
            holder.what = (TextView)row.findViewById(R.id.what);
            holder.acceptButton = (Button)row.findViewById(R.id.accBtn);
            holder.declineButton = (Button)row.findViewById(R.id.deceBtn);


            row.setTag(holder);
        }
        else
        {
            holder = (DeliveryHolder)row.getTag();
        }

        this.lookForUser = reqList.get(position).getLookForUser();
        holder.who.setText("Request from "+
                this.reqList.get(position).getLookForUser().getUser().getFullName());
//        final Request current = reqList.get(position);


        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference newPostRef2 = mDataBase.child("Request").
                        child(reqList.get(position).getKey()).child("status");
                newPostRef2.setValue("DECLINED");

                SendNotification sendNotification = new SendNotification();
                sendNotification.sendMessage(reqList.get(position).getLookForUser().getUser().getFcmToken(),
                        "FrienDeliver,",delUser.getUser().getFullName()+" rejected your request",
                        "FrienDeliver","FrienDeliver");

                reqList.remove(position);
                notifyDataSetChanged();


            }
        });


       // holder.what.setText(current.getLookForUser().getUser().fullName);
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Duty duty = new Duty(reqList.get(position).getLookForUser(),delUser);
                DatabaseReference newPostRef6 = mDataBase.child("Duty").push();
                String dutyKey = newPostRef6.getKey();
                newPostRef6.setValue(duty);

                DatabaseReference newPostRef1 = mDataBase.child("Delivery").
                        child(reqList.get(position).getDeliveryID()).child("status");
                newPostRef1.setValue("ACCEPTED");

                DatabaseReference newPostRef2 = mDataBase.child("Request").
                        child(reqList.get(position).getKey()).child("status");
                newPostRef2.setValue("ACCEPTED");

                DatabaseReference newPostRef3 = mDataBase.child("LookForUser").
                        child(reqList.get(position).getLookUserID()).child("delivery").
                        child("status");
                newPostRef3.setValue("ACCEPTED");

                DatabaseReference newPostRef8 = mDataBase.child("LookForUser").
                        child(reqList.get(position).getLookUserID()).
                        child("dutyId");
                newPostRef8.setValue(dutyKey);

                DatabaseReference newPostRef9 = mDataBase.child("LookForUser").
                        child(reqList.get(position).getLookUserID()).
                        child("delToken");
                newPostRef9.setValue(delUser.getUser().getFcmToken());


                DatabaseReference newPostRef4 = mDataBase.child("LookForUser").
                        child(reqList.get(position).getLookUserID()).child("delivery").
                        child("deliverUserUid");
                newPostRef4.setValue(reqList.get(position).getDelUserID());

                DatabaseReference newPostRef5 = mDataBase.child("Delivery").
                        child(reqList.get(position).getDeliveryID()).child("deliverUserUid");
                newPostRef5.setValue(reqList.get(position).getDelUserID());

//                Duty duty = new Duty(reqList.get(position).getLookForUser(),delUser);
//                DatabaseReference newPostRef6 = mDataBase.child("Duty").push();
//
//                newPostRef6.setValue(duty);
                SendNotification sendNotification = new SendNotification();
                sendNotification.sendMessage(
                        reqList.get(position).getLookForUser().getUser().getFcmToken(),
                        "FrienDeliver,",delUser.getUser().getFullName()+" accepted your request",
                        "FrienDeliver","FrienDeliver");

                reqList.remove(position);
                notifyDataSetChanged();

                //accepting a request


//                Map updatedUserData = new HashMap();
//                updatedUserData.put("Delivery/"+current.getLookForUser().getDelivery().getKey()+"/deliverUserId",);
//                updatedUserData.put("posts/" + newPostKey, newPost);
//
//
//                current.getLookForUser().getDelivery().setStatus("ACCEPTED");
//                FirebaseDatabase ref = mDataBase.child("DeliverUser").child()


            }
        });


        return row;
    }

    private void cont(int position) {
//        DatabaseReference ref  = mDataBase.child("LookForUser").
//                child(reqList.get(position).getLookUserID());
//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                LookForUser ls = dataSnapshot.getValue(LookForUser.class);
//                setHolderWhat(ls);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        DatabaseReference ref2  = mDataBase.child("DeliverUser").
                child(reqList.get(position).getDelUserID());
        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DeliverUser d = dataSnapshot.getValue(DeliverUser.class);
                setDelUser(d);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

//    public void setHolderWhat(LookForUser ls) {
//        this.lookForUser = ls;
//        System.out.println("in set holderrrrr");
//        //holder.what.setText("Request from "+lookForUser.getUser().getFullName());
//        //holder.who.setText(ls.getUser().getFullName());
//
//
//    }

    public void setDelUser(DeliverUser delUser) {
        this.delUser = delUser;
    }


    static class DeliveryHolder
    {

        //ImageView imgIcon;

        TextView what;
        Button acceptButton;
        Button declineButton;
        TextView who;
        //Button acceptButton;
    }

}
