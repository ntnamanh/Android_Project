package com.example.hung.myapplication.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.hung.myapplication.ActionListeners.Functions;
import com.example.hung.myapplication.Model.TrackingInformation;
import com.example.hung.myapplication.R;


import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class TrackableAdapter extends ArrayAdapter<TrackingInformation.Trackingwithaddress> {
    private Context mcontext;
    private List<TrackingInformation.Trackingwithaddress> trackingitem;
    private TextView txtaddress,txtDate,txtstoptime,txttimestop;
    private ImageView image;
    int mResource;


    public TrackableAdapter(@NonNull Context context, int resource, @NonNull List<TrackingInformation.Trackingwithaddress> objects) {
        super(context, resource, objects);
        this.mcontext = context;
        mResource = resource;
        trackingitem = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        int ID = getItem(position).trackingInfo.trackableId;
        Date date = getItem(position).trackingInfo.date;
        String StringDate = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM).format(date);
        String address = getItem(position).address;
        String StopTime =String.valueOf(getItem(position).trackingInfo.stopTime);
        LayoutInflater inflater =  LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mResource,parent,false);
        txtDate = (TextView)convertView.findViewById(R.id.txtxdate);
        txtstoptime= (TextView)convertView.findViewById(R.id.txtstoptime);
        txttimestop = (TextView)convertView.findViewById(R.id.txttimestop);
        image = (ImageView)convertView.findViewById(R.id.imagetruck);
        txtaddress = (TextView)convertView.findViewById(R.id.txtaddress);
        long stoped_time = Functions.monitor_food_truck(date,Integer.parseInt(StopTime));
        if(stoped_time > 0){
            image.setImageResource(R.drawable.icon_food_truck);
            txttimestop.setText("Stoped for: "+stoped_time +" mins");
        }
        else{
            txttimestop.setText("Status: Left");
        }
        txtDate.setText("Date: "+StringDate);
        txtstoptime.setText("Stop Time: "+StopTime + " mins");
        txtaddress.setText("Location: "+address);
        convertView.setTag(getItem(position));
        return convertView;

    }
}
