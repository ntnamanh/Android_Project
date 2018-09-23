package com.example.hung.myapplication.Model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.example.hung.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TrackableService {

    private static final String LOG_TAG = TrackingService.class.getName();
    private static Context context;
    private List<TrackableInfo> newTrackablelist = new ArrayList<>();


    public static class TrackableInfo{
        public int Id;
        public String Name;
        public String Description;
        public String Url;
        public String Category;


        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String url) {
            Url = url;
        }

        public String getCategory() {
            return Category;
        }

        public void setCategory(String category) {
            Category = category;
        }
    }



    private TrackableService(){

    }

    private static class LazyHolder
    {
        static final TrackableService INSTANCE = new TrackableService();
    }

    // PUBLIC METHODS

    // singleton
    // thread safe lazy initialisation: see https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    public static TrackableService getSingletonInstance(Context context)
    {
        TrackableService.context = context;
        return TrackableService.LazyHolder.INSTANCE;
    }
    private void parseFile(Context context)
    {
        newTrackablelist.clear();
        // resource reference to tracking_data.txt in res/raw/ folder of your project
        // supports trailing comments with //
        try (Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.food_truck_data)))
        {
            // match comma and 0 or more whitespace OR trailing space and newline
            scanner.useDelimiter(",\"|\",\"|\"\\n+");
            while (scanner.hasNext())
            {
                TrackableInfo newtrackablelist = new TrackableInfo();
                newtrackablelist.Id = scanner.nextInt();
                newtrackablelist.Name = String.valueOf(scanner.next());
                newtrackablelist.Description = String.valueOf(scanner.next());
                newtrackablelist.Url = String.valueOf(scanner.next());
                newtrackablelist.Category = String.valueOf(scanner.next());
                newTrackablelist.add(newtrackablelist);

            }

        }
        catch (Resources.NotFoundException e)
        {
            Log.i(LOG_TAG, "File Not Found Exception Caught");
        }

    }
    public void  logAll(){
        log(newTrackablelist);
    }
    public void log(List<TrackableInfo> TrackableInfo){
        // we reparse file contents for latest data on every call
        parseFile(context);
    }

    public List<String>  retreive_data_by_category(String catefory){
        List<String> searched = new ArrayList<>();
        for ( TrackableService.TrackableInfo trackable: newTrackablelist) {
            if(catefory.equals("All")){
                searched.add(trackable.Name);
            }
            if(trackable.Category.equals(catefory)){
                searched.add(trackable.Name);
            }
        }
        return searched;
    }

    public List<String> getcategory (){
        List<String> category = new ArrayList<>();
        category.add("All");
        for ( TrackableService.TrackableInfo trackable: newTrackablelist) {
                if(!category.contains(trackable.Category))
                category.add(trackable.Category);
        }
        return category;
    }
    public List<String> getname(){
        List<String> name = new ArrayList<>();
        for ( TrackableService.TrackableInfo trackable: newTrackablelist) {
            name.add(trackable.Name);
        }
        return name;
    }

    public TrackableInfo getelementbyname(String name) {
        List<TrackableInfo> element = new ArrayList<>();
        for ( TrackableService.TrackableInfo trackable: newTrackablelist) {
            if(trackable.Name.equals(name)){
                return trackable;
            }
        }
        return null;
    }
    public String get_name_by_id(int id){
        TrackableInfo trackableInfo = newTrackablelist.get(id-1);
        return  trackableInfo.Name;
    }
}
