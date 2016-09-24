package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Israel Rozen on 15/09/2016.
 */


public class MyDeliveriesAdapter extends ArrayAdapter<LookForUser> {

    private DatabaseReference mDatabase;
    Context context;
    int layoutResourceId;
    ArrayList<LookForUser> deliveries;


    public MyDeliveriesAdapter(Context context, int layoutResourceId, ArrayList<LookForUser> deliveries) {
        super(context,layoutResourceId, deliveries);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.deliveries = deliveries;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeliveryHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeliveryHolder();
            holder.what = (TextView)row.findViewById(R.id.what);
            holder.path = (TextView)row.findViewById(R.id.path);
            holder.deleteButton = (Button)row.findViewById(R.id.deleteBtn);

           // holder.from = (TextView)row.findViewById(R.id.fromWhere);


            row.setTag(holder);
        }
        else
        {
            holder = (DeliveryHolder)row.getTag();
        }


        LookForUser current = deliveries.get(position);
        String t = current.getDelivery().getKey();
        String q = current.getKey();
        holder.what.setText(current.getDelivery().getDesc());
        holder.path.setText("From "+current.getDelivery().getCityDepart()+
                " to "+current.getDelivery().getCityArrive());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Do you want to cancel the delivery?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                LookForUser current = deliveries.get(position);
                                String t = deliveries.get(position).getDelivery().getKey();
                                String q = deliveries.get(position).getKey();
                                String k = deliveries.get(position).getDelivery().getDeliverUserUid();
                                String m = deliveries.get(position).getDutyId();
                                System.out.println(q);
//                                DatabaseReference newPostRef1 = mDatabase.child("Delivery").
//                                        child(t).child("status");
//                                newPostRef1.setValue("CANCEL");

                                DatabaseReference newPostRef2 = mDatabase.child("LookForUser").
                                        child(q);
                                newPostRef2.removeValue();

                                DatabaseReference newPostRef3 = mDatabase.child("Duty").
                                        child(m);
                                newPostRef3.removeValue();
//                                        child("status");
//                                newPostRef2.setValue("CANCEL");


                                deliveries.remove(position);
                                notifyDataSetChanged();
                                //dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                               dialog.cancel();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();

                alert.show();

            }
        });




        return row;
    }




    static class DeliveryHolder
    {

        //ImageView imgIcon;
        TextView what;
        TextView path;
        Button deleteButton;
       // TextView from;
        //Button acceptButton;
    }
}
