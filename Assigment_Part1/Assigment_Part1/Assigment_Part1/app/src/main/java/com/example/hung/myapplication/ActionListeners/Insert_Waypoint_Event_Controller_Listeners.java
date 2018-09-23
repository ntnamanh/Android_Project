package com.example.hung.myapplication.ActionListeners;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hung.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Insert_Waypoint_Event_Controller_Listeners implements OnClickListener {

    private Context context;
    private TextView time;
    private Calendar currentime;
    private TimePickerDialog.OnTimeSetListener timeDataset;
    private TimePickerDialog d;
    private int hour,minute;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private Dialog popup;

    public Insert_Waypoint_Event_Controller_Listeners(){

    }
    public Insert_Waypoint_Event_Controller_Listeners(Context mcontext,TextView t,Dialog dialog ){
        context = mcontext;
        time = t;
        popup = dialog;

    }

    @Override
    public void onClick(View v) {
        //get the current date

        if(v.getId() == R.id.txttime){
            currentime = Calendar.getInstance();
            hour = currentime.get(Calendar.HOUR);
            minute = currentime.get(Calendar.MINUTE);
            timeDataset = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    Functions.showTime(hourOfDay,minute,time);
                }
            };
            d = new TimePickerDialog(context,timeDataset,hour,minute,false);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.show();
        }

        if(v.getId()==R.id.btngetlocation){
            Functions.show_popup_select_location(context,v,popup);
        }

    }

}
