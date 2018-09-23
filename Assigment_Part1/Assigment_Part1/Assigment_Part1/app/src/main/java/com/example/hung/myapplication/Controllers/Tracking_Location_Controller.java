package com.example.hung.myapplication.Controllers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hung.myapplication.Model.TrackingInformation;
import com.example.hung.myapplication.R;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class Tracking_Location_Controller extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks  {
    private GoogleApiClient client;
    private GoogleMap mMap;
    private TrackingInformation trackingInformation;
    private int get_id;
    private String Name;
    private boolean status;
    private double lon,lat,truck_lon,truck_lat;
    private Location mLastLocation;
    private LatLng mLatLon,checknewlocation;

    private Marker myMarker,truckMarker;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).build();
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        trackingInformation = TrackingInformation.getSingletonInstance(getApplicationContext());
        get_id = getIntent().getIntExtra("TrackableID",-1);
        Name = getIntent().getStringExtra("TrackableName");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

    }
    protected void onStart() {
        super.onStart();
        client.connect();
    }

    public void startLocation(){
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, new MyLocationListener());
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    client);
            if (mLastLocation != null) {
                lat =mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
                mLatLon = new LatLng(lat,lon);

                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(mLatLon)
                        .title("your location")
                );

                if(trackingInformation.check_date(get_id)){
                    Toast.makeText(this, Name+" truck is being in service!!!", Toast.LENGTH_SHORT).show();
                    status=true;
                    if(trackingInformation.location_food_truck(get_id) !=null){
                        checknewlocation = trackingInformation.location_food_truck(get_id);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trackingInformation.location_food_truck(get_id), 16));
                        truckMarker =  mMap.addMarker(new MarkerOptions()
                                .position(trackingInformation.location_food_truck(get_id))
                                .title(Name).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_food_truck))
                        );
                    }
                    else{
                        Toast.makeText(Tracking_Location_Controller.this, Name+" truck is moving to new location!!!", Toast.LENGTH_SHORT).show();
                        truckMarker = myMarker;
                    }
                }
                else{
                    status=false;
                    Toast.makeText(this, Name+" truck is out of service!!!", Toast.LENGTH_SHORT).show();
                }



            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        startLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            mLatLon = new LatLng(lat,lon);
            if(status){
                if(trackingInformation.location_food_truck(get_id)==null){
                    Toast.makeText(Tracking_Location_Controller.this, Name+" truck is moving to new location!!!", Toast.LENGTH_SHORT).show();
                    truckMarker.remove();
                }
                else{
                    if(!checknewlocation.toString().equals(trackingInformation.location_food_truck(get_id).toString()))
                    {
                        Toast.makeText(Tracking_Location_Controller.this, Name+" truck is at new location!!!", Toast.LENGTH_SHORT).show();
                        checknewlocation = trackingInformation.location_food_truck(get_id);
                    }
                    truckMarker.remove();
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(trackingInformation.location_food_truck(get_id), 16));
                    truckMarker =  mMap.addMarker(new MarkerOptions()
                            .position(trackingInformation.location_food_truck(get_id))
                            .title(Name).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_food_truck))
                    );
                }
            }


            myMarker.remove();
            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLon, 16));
            myMarker = mMap.addMarker(new MarkerOptions()
                    .position(mLatLon)
                    .title("your location")
            );
            Log.d("--", "onlocationchagned");

        }
    }
}
