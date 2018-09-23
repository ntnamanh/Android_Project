package com.example.hung.myapplication.ActionListeners;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.hung.myapplication.Model.TrackableService;

public class List_Trackable_Controller_All_Listeners implements View.OnClickListener,AdapterView.OnItemClickListener,Spinner.OnItemSelectedListener {
    private TrackableService trackableService;
    private ListView trackablelist;
    private Dialog popup;
    private ArrayAdapter<String> spinner_array;
    private Context context;
    private Activity activity;



    public List_Trackable_Controller_All_Listeners(){

    }
    public List_Trackable_Controller_All_Listeners(Context mcontext , Spinner spinner, ListView track, Activity ac){
        context = mcontext;
        trackableService = TrackableService.getSingletonInstance(mcontext);
        activity =ac;
        trackablelist = track;
        popup = new Dialog(context);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TrackableService.TrackableInfo element = trackableService.getelementbyname(parent.getItemAtPosition(position).toString());
            Functions.showpopup(context,view,popup,element);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String category = parent.getItemAtPosition(position).toString();
        spinner_array = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,trackableService.retreive_data_by_category(category));
        trackablelist.setAdapter(spinner_array);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
