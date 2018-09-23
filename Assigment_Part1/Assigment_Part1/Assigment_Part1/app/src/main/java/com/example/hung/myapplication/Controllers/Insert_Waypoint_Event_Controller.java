package com.example.hung.myapplication.Controllers;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hung.myapplication.ActionListeners.Insert_Waypoint_Event_Controller_Listeners;
import com.example.hung.myapplication.R;

import java.util.Dictionary;

public class Insert_Waypoint_Event_Controller extends AppCompatActivity {
    private TextView date,trackableID,time,txtlat,txtlon;
    private EditText Stoptime;
    private String get_date;
    private Button select_location,save;
    private Dialog dialog;
    private int get_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert__waypoint__event__controller);
        date = (TextView)findViewById(R.id.txtcreatedate);
        trackableID = (TextView)findViewById(R.id.txttrackableID);
        select_location = (Button)findViewById(R.id.btngetlocation);
        time = (TextView)findViewById(R.id.txttime);
        dialog = new Dialog(this);
        Stoptime =(EditText) findViewById(R.id.edttimestop);
        txtlat = (TextView)findViewById(R.id.txtlat);
        txtlon = (TextView)findViewById(R.id.txtlon);
        get_id = getIntent().getIntExtra("id",-1);
        get_date = getIntent().getStringExtra("date");
        date.setText(get_date);
        trackableID.setText(String.valueOf(get_id));

        time.setOnClickListener(new Insert_Waypoint_Event_Controller_Listeners(this,time,dialog));
        select_location.setOnClickListener(new Insert_Waypoint_Event_Controller_Listeners(this,time,dialog));


    }
}
