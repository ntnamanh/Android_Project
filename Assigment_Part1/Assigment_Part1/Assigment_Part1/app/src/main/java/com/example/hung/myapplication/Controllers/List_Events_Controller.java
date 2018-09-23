package com.example.hung.myapplication.Controllers;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.hung.myapplication.ActionListeners.List_Events_Controller_All_Listeners;
import com.example.hung.myapplication.Model.TrackingInformation;
import com.example.hung.myapplication.Model.TrackingService;
import com.example.hung.myapplication.R;

public class List_Events_Controller extends AppCompatActivity {
    private TrackingService trackingService;
    private TrackingInformation trackingInformation;
    private ListView list;
    private Spinner date_spinner;
    private Dialog popup_create;
    private int get_id;
    private ArrayAdapter<String> adapter_spinner;
    private ImageButton imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking__list);
        get_id = getIntent().getIntExtra("TrackableID",-1);
        trackingService = TrackingService.getSingletonInstance(getApplicationContext());
        trackingInformation = TrackingInformation.getSingletonInstance(getApplicationContext());
        imageButton = (ImageButton)findViewById(R.id.imageinsert);
        popup_create = new Dialog(this);
        list = (ListView)findViewById(R.id.trackinglist);
        date_spinner = (Spinner)findViewById(R.id.datespinner);
        adapter_spinner = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,trackingInformation.get_List_date(get_id));
        date_spinner.setAdapter(adapter_spinner);
        date_spinner.setOnItemSelectedListener(new List_Events_Controller_All_Listeners(this,List_Events_Controller.this,date_spinner,
                imageButton,list,popup_create,get_id));
        list.setOnItemClickListener(new List_Events_Controller_All_Listeners(this,List_Events_Controller.this,date_spinner,
                                                imageButton,list,popup_create,get_id));

    }
}
