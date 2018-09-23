package com.example.hung.myapplication.ActionListeners;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hung.myapplication.Controllers.Insert_Waypoint_Event_Controller;
import com.example.hung.myapplication.Controllers.Tracking_Location_Controller;
import com.example.hung.myapplication.Controllers.List_Events_Controller;
import com.example.hung.myapplication.Model.TrackableService;
import com.example.hung.myapplication.Model.TrackingInformation;
import com.example.hung.myapplication.R;
import com.google.android.gms.maps.MapView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Functions  {
    private static String format;
    private String address;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    static Calendar currentime;
    static TextView date;
    static DatePickerDialog.OnDateSetListener dateSetListener;
    public static void showpopup(final Context context, View v, final Dialog popup, final TrackableService.TrackableInfo list){
        popup.setContentView(R.layout.popup_layout);

        ImageView close = (ImageView)popup.findViewById(R.id.image);

//        ImageView picture = (ImageView)popup.findViewById(R.id.picture);

//        load_image(picture,list.Name);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        TextView Name = (TextView)popup.findViewById(R.id.txtName);

        TextView Descip = (TextView)popup.findViewById(R.id.txtDescription);

        TextView Url = (TextView)popup.findViewById(R.id.txtUrl);

        TextView Cate = (TextView)popup.findViewById(R.id.txtCategory);

        Button tracking = (Button)popup.findViewById(R.id.btntrackinglist);

        Button make_tracking = (Button)popup.findViewById(R.id.btnlocation);

        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,List_Events_Controller.class);
                intent.putExtra("TrackableID",list.Id);
                context.startActivity(intent);
            }
        });

        make_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Tracking_Location_Controller.class);
                intent.putExtra("TrackableID",list.Id);
                intent.putExtra("TrackableName",list.Name);
                context.startActivity(intent);
            }
        });

        Name.setText(list.Name);

        Descip.setText("Description: "+list.Description);

        Url.setText("URL: "+list.Url);

        Cate.setText("Category: "+list.Category);

        popup.show();
    }

    public static void show_popup_delete_event(final Context context, final String date, final int trackableID, final Dialog dialog, final Activity activity){
        final TrackableService trackableService = TrackableService.getSingletonInstance(context);
        final TrackingInformation trackingInformation = TrackingInformation.getSingletonInstance(context);
        dialog.setContentView(R.layout.popup_layout_delete_edit_events);
        ImageView cancel = (ImageView)dialog.findViewById(R.id.imagecancel);
        Button Delete = (Button)dialog.findViewById(R.id.btnDelete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingInformation.delete_date(date,trackableID);
                Toast.makeText(context, "The event is deleted!!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                activity.finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView txtconfirm = (TextView)dialog.findViewById(R.id.txtConfirm1);
        txtconfirm.setText("Are you sure you want to delete "+ date + " event of "+trackableService.get_name_by_id(trackableID));
        dialog.show();
    }

    public static void show_popup_Insert_Event(final Context context, final Dialog dialog, final List<String> addnewdate, final Spinner spinner, View view){
        currentime = Calendar.getInstance();
        dialog.setContentView(R.layout.popup_insert_event);
        ImageView imageButton = (ImageView) dialog.findViewById(R.id.imagecancel1);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button buttoncreate = (Button)dialog.findViewById(R.id.btnCreate);
        buttoncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(check_available_date(date.getText().toString())){
                        addnewdate.add(date.getText().toString());
                        ArrayAdapter<String> adapter_spinner = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,addnewdate);
                        spinner.setAdapter(adapter_spinner);
                        dialog.dismiss();
                        Toast.makeText(context, "Please select the date in drop downlist to add waypoint to your event!!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Your selected day is invalid!!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //spinner.setAdapter();
            }
        });
         date =  (TextView)dialog.findViewById(R.id.txtselectdate);
        final String current = simpleDateFormat.format(currentime.getTime());
        date.setText(current);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentime = Calendar.getInstance();
                int year = currentime.get(Calendar.YEAR);
                int month = currentime.get(Calendar.MONTH);
                int day = currentime.get(Calendar.DATE);
                DatePickerDialog dialog1 = new DatePickerDialog(dialog.getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,dateSetListener,year,month,day);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog1.show();

            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Date a = null;
                try {
                    a = simpleDateFormat.parse(String.valueOf(month+1)+"/"+String.valueOf(dayOfMonth)+"/"+String.valueOf(year));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                date.setText(simpleDateFormat.format(a));
            }
        };
        dialog.show();
    }

    public static void show_popup_select_location(final Context context, View v, final Dialog popup){
        MapView mv = (MapView)popup.findViewById(R.id.mapView);
        
        popup.setContentView(R.layout.popup_select_location_layout);
        popup.show();

    }

    //This function will return the number of stop time in minutes.
    public static long monitor_food_truck(Date date, int stoptime){
        if(stoptime <=0)
            return 0;
        else
        {
            //get the current time
            currentime = Calendar.getInstance();
            String date_move  = simpleDateFormat.format(date);
            String currentdate = simpleDateFormat.format(currentime.getTime());
            //check the current date and the selected date. if the use choose the date which is today the program will return the number of minutes
            if(currentdate.equals(date_move)){
                //check the current time is in range of start time and end time.
                if(System.currentTimeMillis()>= date.getTime() && System.currentTimeMillis()<= (date.getTime()+(stoptime*60000)))
                    return (((System.currentTimeMillis())-date.getTime())/60000);
        }
        }
        return 0;
    }
    //check date
    public static boolean check_date(String date){
        currentime = Calendar.getInstance();
        String currentdate = simpleDateFormat.format(currentime.getTime());
        if(currentdate.equals(date))
            return true;
        return false;
    }
    //check the available date when the user want to make the plan tracking. They are allowed to choose the available days. It means from today to day after
    public static boolean check_available_date(String date) throws ParseException {
        currentime = Calendar.getInstance();
        String current = simpleDateFormat.format(currentime.getTime());
        Date date1 = simpleDateFormat.parse(date);
        Date date2 = simpleDateFormat.parse(current);

        if(date1.getTime()>=date2.getTime()){
            return true;
        }
        return false;
    }

    public static void insert_Event(final Context mcontext, ImageButton imageButton, final List<String> listdate , View v, final Spinner spinner, final Dialog popup_insert){


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               show_popup_Insert_Event(mcontext,popup_insert,listdate,spinner,v);
            }
        });
    }
    public static void insert_Waypoint(final Context mcontext, ImageButton imageButton, View v, final String date, final int trackableid){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, Insert_Waypoint_Event_Controller.class);
                intent.putExtra("date",date);
                intent.putExtra("id",trackableid);
                mcontext.startActivity(intent);
            }
        });
    }
    public static void showTime(int hour, int min,TextView time) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        time.setText(new StringBuilder().append(String.format("%02d",hour)).append(":").append(String.format("%02d",min)).append(":").append("00")
                .append(" ").append(format));
    }


}


