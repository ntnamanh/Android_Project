package com.example.hung.myapplication.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.hung.myapplication.HttpHandlerJsondata.Request;
import com.example.hung.myapplication.HttpHandlerJsondata.Result;
import com.example.hung.myapplication.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static com.example.hung.myapplication.HttpHandlerJsondata.HttpHandler.doGet;

public class TrackingInformation {
    private  String url;
    private static final String LOG_TAG = TrackingService.class.getName();
    private List<TrackingInformation.Trackingwithaddress> trackingaddress = new ArrayList<>();
    private static Context mcontext;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private Calendar currentime;
    private LatLng newlonlat;
    private TrackingService trackingService = TrackingService.getSingletonInstance(mcontext);

    public static class Trackingwithaddress{
        public TrackingService.TrackingInfo trackingInfo;
        public String address;
    }

    private TrackingInformation(){

    }

    private static class LazyHolder{
        static final TrackingInformation INSTANCE = new TrackingInformation();
    }

    public static TrackingInformation getSingletonInstance(Context context){
            TrackingInformation.mcontext = context;
            return LazyHolder.INSTANCE;
    }

    private void getdatafromTrackingService(){
        trackingaddress.clear();
        Readaddress readaddress = new Readaddress(trackingaddress);
        readaddress.execute();
    }
    public void logAll()
    {
        log(trackingaddress);
    }

    // log contents of provided list (for testing/logging and example purposes only)
    public void log(List<TrackingInformation.Trackingwithaddress> trackingListaddress)
    {
        // we reparse file contents for latest data on every call
        getdatafromTrackingService();
        for (TrackingInformation.Trackingwithaddress trackingaddress : trackingListaddress)
        {
            // to prevent this logging issue https://issuetracker.google.com/issues/77305804
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
            }

        }
    }

    private class Readaddress  extends AsyncTask<Void, Integer, Void> {
        String address="",gurl;
        List<TrackingInformation.Trackingwithaddress> newlist;

        public Readaddress(List<TrackingInformation.Trackingwithaddress> list){
            newlist = list;
        }
        protected Void doInBackground(Void...prams) {
            for (TrackingService.TrackingInfo trackinginfo: trackingService.get_all_data()) {
                url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+trackinginfo.latitude+","+trackinginfo.longitude+"&sensor=true";

                gurl = doGet(url);
                Request request = new Gson().fromJson(gurl,Request.class);

                for (Result a: request.results){
                    address = a.formatted_address;
                    break;
                }

                Trackingwithaddress trackingwithaddress = new Trackingwithaddress();
                trackingwithaddress.trackingInfo = trackinginfo;
                trackingwithaddress.address = address;
                trackingaddress.add(trackingwithaddress);
            }
            return null;
        }
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate();
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
    public List<String> get_List_date(int id){
        List<String> list_date = new ArrayList<String>();
        list_date.add("All_Events");
        for (Trackingwithaddress trackinginfo: trackingaddress) {
            if(trackinginfo.trackingInfo.trackableId == id){
                if(!list_date.contains(simpleDateFormat.format(trackinginfo.trackingInfo.date))){
                    list_date.add(  simpleDateFormat.format(trackinginfo.trackingInfo.date));
                }
            }
        }
        return list_date;
    }

    public List<String> get_date(int id){
        List<String> list_date = new ArrayList<String>();
        for (Trackingwithaddress trackinginfo: trackingaddress) {
            if(trackinginfo.trackingInfo.trackableId == id){
                if(!list_date.contains(simpleDateFormat.format(trackinginfo.trackingInfo.date))){
                    list_date.add(  simpleDateFormat.format(trackinginfo.trackingInfo.date));
                }
            }
        }
        return list_date;
    }

    public List<TrackingInformation.Trackingwithaddress> select_date(String date, int trackingID){
        List<TrackingInformation.Trackingwithaddress> list_by_date = new ArrayList<>();
        for (Trackingwithaddress trackinginfo: trackingaddress) {
            if(simpleDateFormat.format(trackinginfo.trackingInfo.date).equals(date) && trackinginfo.trackingInfo.trackableId == trackingID)
                list_by_date.add(trackinginfo);
        }
        return list_by_date;
    }

    public boolean check_date(int ID){
        int count=0;
        Calendar currentime = Calendar.getInstance();
        String currentdate = simpleDateFormat.format(currentime.getTime());
        for (Trackingwithaddress trackinginfo: trackingaddress ) {
            if(simpleDateFormat.format(trackinginfo.trackingInfo.date).equals(currentdate) && trackinginfo.trackingInfo.trackableId == ID) {
                if(System.currentTimeMillis()<= trackinginfo.trackingInfo.date.getTime()) {
                    count++;
                    break;
                }
            }
        }
        if(count>0)
            return true;
        else
            return false;
    }
    public LatLng location_food_truck(int ID) {
        for (Trackingwithaddress trackinginfo : trackingaddress) {
            currentime = Calendar.getInstance();
            String date_move = simpleDateFormat.format(trackinginfo.trackingInfo.date);
            String currentdate = simpleDateFormat.format(currentime.getTime());
            if (currentdate.equals(date_move) && trackinginfo.trackingInfo.trackableId == ID) {
                if (trackinginfo.trackingInfo.stopTime <= 0) {
                    continue;
                } else {
                    if (System.currentTimeMillis() >= trackinginfo.trackingInfo.date.getTime() && System.currentTimeMillis() <= (trackinginfo.trackingInfo.date.getTime() + (trackinginfo.trackingInfo.stopTime * 60000))) {
                        newlonlat = new LatLng(trackinginfo.trackingInfo.latitude,trackinginfo.trackingInfo.longitude);
                        return newlonlat;
                    } else
                        continue;
                }
            }
        }
        return null;
    }
    public void delete_date(String date,int ID){
        Iterator<Trackingwithaddress> trackingInfoIterator = trackingaddress.iterator();
        while (trackingInfoIterator.hasNext()){
            Trackingwithaddress element =trackingInfoIterator.next();
            if(simpleDateFormat.format(element.trackingInfo.date).equals(date)&&element.trackingInfo.trackableId==ID){
                trackingInfoIterator.remove();
            }
        }
    }
}
