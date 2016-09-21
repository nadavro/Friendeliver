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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Israel Rozen on 15/09/2016.
 */


public class MyDeliveriesAdapter extends ArrayAdapter<LookForUser> {

    Context context;
    int layoutResourceId;
    ArrayList<LookForUser> deliveries;


    public MyDeliveriesAdapter(Context context, int layoutResourceId, ArrayList<LookForUser> deliveries) {
        super(context,layoutResourceId, deliveries);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.deliveries = deliveries;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeliveryHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeliveryHolder();
            holder.what = (TextView)row.findViewById(R.id.txtTitle);
           // holder.from = (TextView)row.findViewById(R.id.fromWhere);


            row.setTag(holder);
        }
        else
        {
            holder = (DeliveryHolder)row.getTag();
        }


        LookForUser current = deliveries.get(position);


        //System.out.println("1");
        // getUserName(deliverUser);
        //System.out.println(deliverUser.getFullName());
        holder.what.setText( deliveries.get(position).getDelivery().getDesc()+" from "+
                deliveries.get(position).getDelivery().getCityDepart()+" to "+
                deliveries.get(position).getDelivery().getCityArrive());
//                +current.getDelivery().getCityDepart()+" to "+
//                current.getDelivery().getCityArrive());
        //holder.from.setText(" from "+del.getCityDepart()+" to "+del.getCityArrive());
       // holder.acceptButton.setText("Ask");

        //holder.path.setText("Is going from "+deliverUser.getCityDepart()+" to "+deliverUser.getCityArrive());



        //holder.imgIcon.setImageResource(weather.icon);
        //System.out.println("3");


        return row;
    }




    static class DeliveryHolder
    {

        //ImageView imgIcon;
        TextView what;
       // TextView from;
        //Button acceptButton;
    }
}
