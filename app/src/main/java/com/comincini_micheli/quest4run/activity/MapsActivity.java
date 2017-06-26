package com.comincini_micheli.quest4run.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.comincini_micheli.quest4run.objects.Gps;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.comincini_micheli.quest4run.R;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        List<Gps> gpsList = db.getAllGps();
        PolylineOptions line= new PolylineOptions().width(5).color(Color.RED);
        LatLng point = null;
        Double meanLat = 0.0, meanLng = 0.0;
        Double d = Double.parseDouble(
                gpsList.get(0).getLatitude()
        );
        //Log.w("test gps","("+gpsList.get(0).getLatitude()+")"+d);

        double interLat = 0, interLng = 0;
        int counter = 0;
        for(int i=0; i<gpsList.size(); i++){
            if(i>0&&i<(gpsList.size()-1)) {
                interLat = (Double.parseDouble(gpsList.get(i - 1).getLatitude()) + Double.parseDouble(gpsList.get(i + 1).getLatitude())) / 2;
                interLng = (Double.parseDouble(gpsList.get(i - 1).getLongitude()) + Double.parseDouble(gpsList.get(i + 1).getLongitude())) / 2;
            }
            point = new LatLng(
                    Double.parseDouble(
                            gpsList.get(i).getLatitude()
                    ),
                    Double.parseDouble(
                            gpsList.get(i).getLongitude()
                    ));
            LatLng interPoint = new LatLng(interLat,interLng);
            if(i>0&&i<(gpsList.size()-1)&&checkPoint(point, interPoint)){
                if(i==0)
                    mMap.addMarker(new MarkerOptions().position(point).title(i+"").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                else if(i==(gpsList.size()-1))
                    mMap.addMarker(new MarkerOptions().position(point).title(i+"").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                else
                    mMap.addMarker(new MarkerOptions().position(point).title(i+"").anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_black)));
                line.add(point);
                meanLat += point.latitude;
                meanLng += point.longitude;
                counter++;
            }
        }

        meanLat/=counter;
        meanLng/=counter;

        mMap.addPolyline(line);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(meanLat,meanLng),18));
    }

    private boolean checkPoint(LatLng realPoint, LatLng calculatedPoint){
        //TODO eventualmente implementare logica altrimenti rimuovere
        return true;
    }

}
