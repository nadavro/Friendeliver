package com.nadavrozen.myfirebaseauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Israel Rozen on 01/09/2016.
 */
public class MyEditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText _editText;
    private int _day;
    private int _month;
    private int _birthYear;
    private Context _context;

    public MyEditTextDatePicker(Context context, int editTextViewID)
    {
        Log.v("hereee",String.valueOf(editTextViewID));
        Log.v("HERE","HAHAHAHA");
        Activity act = (Activity)context;
        this._editText = (EditText)act.findViewById(editTextViewID);
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        _birthYear = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }
    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        /*
        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        */

        Log.v("asdsd","dssdsdsdsd");
        DatePickerDialog d = new DatePickerDialog(_context, AlertDialog.THEME_HOLO_LIGHT, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker dp = d.getDatePicker();

        //setting the datepicker to maximum CURRENT date
        Calendar calendari = Calendar.getInstance();//get the current day
        dp.setMaxDate(calendar.getTimeInMillis());
        d.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        _editText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
    }
}

