package com.example.hung.myapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.hung.myapplication.ActionListeners.Functions;
import com.example.hung.myapplication.R;

import java.text.ParseException;
import java.util.List;

public class Event_Adapter extends ArrayAdapter<String> {
    private int mResource;
    private Context mcontext;
    private int mtrackableID;
    private TextView txtdate;
    private Dialog popup;
    private ImageButton delete,add;
    private String date;
    private Activity activity;
    public Event_Adapter(@NonNull Context context, int resource, @NonNull List<String> objects, int trackableID, Activity ac) {
        super(context, resource, objects);
        mcontext = context;
        mResource=resource;
        mtrackableID = trackableID;
        activity =ac;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, ViewGroup parent){
        date = getItem(position);
        LayoutInflater Inflater = LayoutInflater.from(mcontext);
        popup = new Dialog(mcontext);
        convertView = Inflater.inflate(mResource,parent,false);
        txtdate = (TextView)convertView.findViewById(R.id.txtdate);
        delete = (ImageButton)convertView.findViewById(R.id.imagedelete);
        add = (ImageButton)convertView.findViewById(R.id.imageadd);
        try {
            if(Functions.check_available_date(date)){
                add.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtdate.setText(date);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.show_popup_delete_event(mcontext,getItem(position),mtrackableID,popup,activity);

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        convertView.setTag(date);
        return convertView;
    }

}
