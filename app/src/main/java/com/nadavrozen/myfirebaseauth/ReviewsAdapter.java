package com.nadavrozen.myfirebaseauth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Israel Rozen on 22/09/2016.
 */
public class ReviewsAdapter extends ArrayAdapter<Review> {

    private final Context context;
    private final int layoutResourceId;
    private final List<Review> revList;
    private final DatabaseReference mDataBase;

    public ReviewsAdapter(Context context, int layoutResourceId, List<Review> objects) {
        super(context, layoutResourceId, objects);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.revList = objects;
        mDataBase =  FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ReviewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ReviewHolder();
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.recommendation = (TextView)row.findViewById(R.id.recommendation);


            row.setTag(holder);
        }
        else
        {
            holder = (ReviewHolder)row.getTag();
        }
        Date date = Calendar.getInstance().getTime();
        //
        // Display a date in day, month, year format
        //
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);

        Review review = revList.get(position);
        holder.name.setText("Reviewed by " + review.getName()+" , "+today);
        holder.recommendation.setText("\"" + review.getRecommendation() + "\"");


        return row;
    }

    static class ReviewHolder
    {
        TextView name;
        TextView recommendation;

    }

}
