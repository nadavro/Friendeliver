package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Israel Rozen on 20/09/2016.
 */
public class MyDutiesAdapter extends ArrayAdapter<Duty> {
    private final Context context;
    private final int layoutResourceId;
    private List<Duty> data;

    public MyDutiesAdapter(Context context, int layoutResourceId, List<Duty> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId =layoutResourceId;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

            row.setTag(holder);
        }
        else
        {
            holder = (DutyHolder)row.getTag();
        }

        Duty duty = data.get(position);
//        holder.txtTitle.setText(duty.getDelUser().getDateStr()+ "  "+
//                duty.getLookForUser().getDelivery().getCityDepart()+" To "
//                +duty.getLookForUser().getDelivery().getCityArrive()+"  "+
//                duty.getLookForUser().getUser().getFullName());
        holder.txtTitle.setText(duty.getLookForUser().getUser().getFullName());
        holder.fromto.setText("From "+duty.getLookForUser().getDelivery().getCityDepart()+" to "+
                duty.getLookForUser().getDelivery().getCityArrive());
        holder.dateto.setText("On "+duty.getDelUser().getDateStr());
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
    }
}
