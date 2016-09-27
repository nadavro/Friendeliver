package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Israel Rozen on 20/09/2016.
 */
public class MyDutiesAdapter extends ArrayAdapter<Duty> {
    private final Context context;
    private final int layoutResourceId;
    private DatabaseReference mDatabase;
    private List<Duty> data;

    public MyDutiesAdapter(Context context, int layoutResourceId, List<Duty> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId =layoutResourceId;
        this.data = objects;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DutyHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DutyHolder();

            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.fromto = (TextView)row.findViewById(R.id.fromto);
            holder.dateto = (TextView)row.findViewById(R.id.dateto);
            holder.cancelButton = (Button)row.findViewById(R.id.cancel);

            row.setTag(holder);
        }
        else
        {
            holder = (DutyHolder)row.getTag();
        }

        final Duty duty = data.get(position);
        final String t = data.get(position).getLookForUser().getKey();
//        holder.txtTitle.setText(duty.getDelUser().getDateStr()+ "  "+
//                duty.getLookForUser().getDelivery().getCityDepart()+" To "
//                +duty.getLookForUser().getDelivery().getCityArrive()+"  "+
//                duty.getLookForUser().getUser().getFullName());
        holder.txtTitle.setText(duty.getLookForUser().getUser().getFullName());
        holder.fromto.setText("From "+duty.getLookForUser().getDelivery().getCityDepart()+" to "+
                duty.getLookForUser().getDelivery().getCityArrive());
        holder.dateto.setText("On "+duty.getDelUser().getDateStr());
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.buttonclick));
//                System.out.println("holaaaaa clicked!!!");
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Cancel the delivery?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SendNotification sendNotification = new SendNotification();
                        sendNotification.sendMessage(duty.getLookForUser().getUser().getFcmToken(),
                                "FrienDeliver",duty.getDelUser().getUser().getFullName()+" canceled your delivery",
                                "FrienDeliver","FrienDeliver");
                        DatabaseReference m = mDatabase.child("LookForUser").child(duty.getLid());
                        m.removeValue();

                        DatabaseReference r = mDatabase.child("Duty").child(duty.getKey());
                        r.removeValue();



                        data.remove(position);
                        notifyDataSetChanged();
                        dialog.cancel();

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        v.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.buttonshape));
                        dialog.cancel();
                    }
                });
                AlertDialog d = builder.create();
                d.show();
            }
        });
        //holder.txtTitle.setText(weather.title);
        //holder.imgIcon.setImageResource(weather.icon);

        return row;
    }

    static class DutyHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView fromto;
        TextView dateto;
        Button cancelButton;
    }
}
