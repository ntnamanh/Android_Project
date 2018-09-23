package com.example.hung.myapplication.Controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hung.myapplication.ActionListeners.List_Trackable_Controller_All_Listeners;
import com.example.hung.myapplication.Model.TrackableService;
import com.example.hung.myapplication.Model.TrackingInformation;
import com.example.hung.myapplication.Model.TrackingService;
import com.example.hung.myapplication.R;

public class List_Trackable_Controller extends AppCompatActivity{
    private TrackableService trackableService;
    private TrackingService trackingService;
    private TrackingInformation trackingInformation;
    private ListView Trackablelist;
    private Spinner select_cate;
    private ArrayAdapter<String> spinner_array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        trackableService = TrackableService.getSingletonInstance(getApplicationContext());
        trackingService = TrackingService.getSingletonInstance(getApplicationContext());
        trackingInformation = TrackingInformation.getSingletonInstance(getApplicationContext());
        trackableService.logAll();
        trackingService.logAll();
        trackingInformation.logAll();
        Toast.makeText(this, "Please, choose the brand you want to track.", Toast.LENGTH_SHORT).show();
        Trackablelist = (ListView)findViewById(R.id.listviewtrackable);
        select_cate = (Spinner)findViewById(R.id.category_spinner);
        spinner_array = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,trackableService.getcategory());
        select_cate.setAdapter(spinner_array);
        Trackablelist.setOnItemClickListener(new List_Trackable_Controller_All_Listeners(this,select_cate,Trackablelist,this));
        select_cate.setOnItemSelectedListener(new List_Trackable_Controller_All_Listeners(this,select_cate,Trackablelist,this));
    }
}
