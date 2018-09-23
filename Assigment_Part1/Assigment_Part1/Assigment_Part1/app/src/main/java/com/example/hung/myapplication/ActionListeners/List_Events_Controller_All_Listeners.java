package com.example.hung.myapplication.ActionListeners;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hung.myapplication.Adapter.Event_Adapter;
import com.example.hung.myapplication.Adapter.TrackableAdapter;
import com.example.hung.myapplication.Model.TrackingInformation;
import com.example.hung.myapplication.Model.TrackingService;
import com.example.hung.myapplication.R;

import java.util.List;

public class List_Events_Controller_All_Listeners implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private TrackingService trackingService;
    private TrackingInformation trackingInformation;
    private ListView list;
    private Spinner date_spinner;
    private TrackableAdapter adapter;
    private Dialog popup_create;
    private boolean check;
    private TrackingInformation.Trackingwithaddress trackingItem;
    private List<TrackingInformation.Trackingwithaddress> list_date;
    private Event_Adapter event_adapter;
    private Thread reload_list;
    private Activity activity;
    private int get_id;
    private ImageButton imageButton;
    private Context context;

    public List_Events_Controller_All_Listeners(){

    }

    public List_Events_Controller_All_Listeners(Context mcontext,Activity ac,Spinner spinner,ImageButton imB,ListView listview,Dialog popup,int id){
        context = mcontext;
        trackingInformation = TrackingInformation.getSingletonInstance(context);
        trackingService = TrackingService.getSingletonInstance(context);
        activity = ac;
        date_spinner = spinner;
        imageButton = imB;
        list = listview;
        popup_create = popup;
        get_id = id;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(date_spinner.getSelectedItem().equals("All_Events")){
            Toast.makeText(context, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
        }
        else{
            trackingItem = (TrackingInformation.Trackingwithaddress)view.getTag();
            Toast.makeText( context,trackingItem.address,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
        String selection_spinner = date_spinner.getItemAtPosition(position).toString();

        if(selection_spinner.equals("All_Events")){
            check = false;

            if(trackingInformation.get_date(get_id).isEmpty()){
                Toast.makeText(context, "No event for this food truck.", Toast.LENGTH_SHORT).show();
            }
            else {
                event_adapter = new Event_Adapter(context, R.layout.list_all_event_layout,trackingInformation.get_date(get_id),get_id,activity);
                list.setAdapter(event_adapter);
            }
            Functions.insert_Event(context,imageButton,trackingInformation.get_List_date(get_id),view,date_spinner,popup_create);
        }
        else{
            Functions.insert_Waypoint(context,imageButton,view,selection_spinner,get_id);
            if(!Functions.check_date(selection_spinner)){
                check = false;
                adapter = new TrackableAdapter(context,R.layout.listtracking,trackingInformation.select_date(parent.getItemAtPosition(position).toString(),get_id));
                list.setAdapter(adapter);
            }
            else{
                check = true;
                adapter = new TrackableAdapter(context,R.layout.listtracking,trackingInformation.select_date(parent.getItemAtPosition(position).toString(),get_id));
                list.setAdapter(adapter);
                reload_list = new Thread(){
                    @Override
                    public void run(){
                        while(!isInterrupted()){
                            try {
                                Thread.sleep(100);

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(check==true){
                                            adapter = new TrackableAdapter(context,R.layout.listtracking,trackingInformation.select_date(parent.getItemAtPosition(position).toString(),get_id));
                                            list.setAdapter(adapter);
                                        }
                                        else{
                                            reload_list.interrupt();
                                        }
                                    }
                                });

                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                };
                reload_list.start();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
