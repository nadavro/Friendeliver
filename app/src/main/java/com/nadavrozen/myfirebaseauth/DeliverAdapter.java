package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
 * Created by Israel Rozen on 12/09/2016.
 */
public class DeliverAdapter extends ArrayAdapter<DeliverUser> {
    private final LookForUser lookForUser;
    private Delivery delivery;
    private DeliverUser current;
    DatabaseReference mDataBase;
    Context context;
    int layoutResourceId;
    DeliverUser[] objects= null;



    //private User user;

    public DeliverAdapter(Context context, int layoutResourceId, DeliverUser[] objects, Delivery delivery, LookForUser lookForUser) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.lookForUser = lookForUser;
        this.layoutResourceId = layoutResourceId;
        this.objects = objects;
        mDataBase =  FirebaseDatabase.getInstance().getReference();
        this.delivery = delivery;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeliverUserHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeliverUserHolder();
           holder.acceptButton = (Button)row.findViewById(R.id.imgIcon);
            holder.path = (TextView)row.findViewById(R.id.deliverPath);
            holder.deliverName = (TextView)row.findViewById(R.id.txtTitle);//
            // holder.acceptButton = (Button)row.findViewById(R.id.acceptButton);

            row.setTag(holder);
        }
        else
        {
            holder = (DeliverUserHolder)row.getTag();
        }

        DeliverUser deliverUser = objects[position];

        this.current = deliverUser;

        //System.out.println("1");
       // getUserName(deliverUser);
        //System.out.println(deliverUser.getFullName());
        holder.deliverName.setText(deliverUser.getUser().fullName);
        holder.acceptButton.setText("Ask");
        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Ask " + objects[position].getUser().fullName + " to pickup your things?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Map<String,Object> taskMap = new HashMap<String,Object>();
                                        //taskMap.put("deliverUserUid", current.getKey());
                                        //taskRef.updateChildren(taskMap);
                                        //System.out.println(current.getName());
                                        //System.out.println(delivery.getKey());
                                       // delivery.setDeliverUserUid(current.getKey());

                                        // System.out.println(delivery.getDeliverUserUid());
                                        //update nmy deliveries
//                                        DatabaseReference ref1 = mDataBase.child("Delivery").
//                                                child(delivery.getKey()).child("deliverUserUid");
//                                        ref1.setValue(current.getKey());

                                        //Noitify the deliverUser about the request
                                        Request request = new Request(lookForUser, objects[position],delivery.getKey());
                                        DatabaseReference ref2 = mDataBase.child("Request").push();
                                        ref2.setValue(request);


//                                        DatabaseReference ref2 = mDataBase.child("DeliverUser").
//                                                child(objects[position].getKey()).child("Requests").push();
//                                                ref2.setValue(request);
//                                        DatabaseReference ref2 = mDataBase.child("DeliverUser").
//                                                child(objects[position].getKey());
//                                        ref2.setValue(objects[position]);

//                                        Map<String,Object> m = new HashMap<String, Object>();
//                                        m.put(objects[position].getKey(),objects[position]);
//                                        ref2.updateChildren(m);

                                        //show alert to the user who was looking for delivery
                                        new AlertDialog.Builder(context)
                                                .setMessage("A request was send to "+
                                                        objects[position].getUser().getFullName()).
                                                setPositiveButton("OK",
                                                        new DialogInterface.OnClickListener(){

                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int which) {
                                                                dialog.cancel();

                                                            }
                                                        }).show();


//                                        System.out.println(delivery.getStrDest());
//                                        delivery.setDeliverUser(current); //update my deliveries
//                                        System.out.println(current.getUid());
//                                        current.appendDelivery(delivery);//Update deliveries of deliverUser in his list of deliveries

                                       // Intent intent = new Intent(context, MyDeliveriesActivity.class);
                                       // context.startActivity(intent);

                                        //..make all other deliverUsers in the list to become grey and clickable
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            //

                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        }).show();
            }
        });
        holder.path.setText("Is going from "+deliverUser.getCityDepart()+" to "+deliverUser.getCityArrive());



        //holder.imgIcon.setImageResource(weather.icon);
        //System.out.println("3");


        return row;
    }




    static class DeliverUserHolder
    {

        //ImageView imgIcon;
        TextView deliverName;
        TextView path;
        Button acceptButton;
    }
}
