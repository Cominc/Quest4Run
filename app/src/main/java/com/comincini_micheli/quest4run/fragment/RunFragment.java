package com.comincini_micheli.quest4run.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.DatabaseErrorHandler;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Gps;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;


public class RunFragment extends Fragment {

    static Location previusLocation = null;
    boolean active = false;
    float totalDistance = 0, intermediateDistance = 0;

    private MapView mMapView;
    private GoogleMap mMapGoogle;

    public RunFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //MAPPA***************************************************************************
        mMapView = (MapView) getActivity().findViewById(R.id.map2);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMapGoogle = googleMap;
                /*
                LatLng sydeney = new LatLng(-34,151);
                mMapGoogle.addMarker(new MarkerOptions().position(sydeney));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydeney).zoom(12).build();
                mMapGoogle.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                */
            }
        });

        //********************************************************************************
        final TextView textViewLocation = (TextView) getView().findViewById(R.id.textview_location);
        final DatabaseHandler db = new DatabaseHandler(getContext());

        final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Toast.makeText(getContext(), "Location changed!", Toast.LENGTH_SHORT).show();
                if(previusLocation!=null) {
                    intermediateDistance = location.distanceTo(previusLocation);
                    location.setSpeed(intermediateDistance / ((location.getTime() - previusLocation.getTime()) / 1000));
                }
                textViewLocation.setText("Latitudine: " + location.getLatitude() + "\n" +
                        "Longitudine: " + location.getLongitude() + "\n" +
                        "Altitudine: " + location.getAltitude() + "\n" +
                        "Velocità: " + location.getSpeed() + " (" + location.hasSpeed() + ")\n" +
                        "Direzione: " + location.getBearing() + " (" + location.hasBearing() + ")\n" +
                        "Provider: " + location.getProvider() + "\n\n" +
                        "Distanza percorsa: " + totalDistance + " m"
                );

                if (active) {
                    Gps newPoint = new Gps(location.getLatitude()+"", location.getLongitude()+"", (int) Math.round(location.getAltitude()), (int)location.getTime());
                    db.addGps(newPoint);
                    totalDistance += intermediateDistance;
                    //******************
                    LatLng newPoisition = new LatLng(location.getLatitude(),location.getLongitude());
                    mMapGoogle.addMarker(new MarkerOptions().position(newPoisition).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_black)));
                    PolylineOptions line= new PolylineOptions().width(5).color(Color.RED);
                    if(previusLocation!=null) {
                        line.add(new LatLng(previusLocation.getLatitude(), previusLocation.getLongitude()));
                    }
                    line.add(newPoisition);
                    mMapGoogle.addPolyline(line);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(newPoisition).zoom(18).bearing(location.getBearing()).build();
                    mMapGoogle.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    //******************
                }

                previusLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(getContext(), "Status change", Toast.LENGTH_SHORT).show();
            }

            public void onProviderEnabled(String provider) {
                Toast.makeText(getContext(), "GPS is working", Toast.LENGTH_SHORT).show();
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(getContext(), "GPS is not working", Toast.LENGTH_SHORT).show();
            }
        };


        Button btnGPS_start = (Button) getView().findViewById(R.id.button_gps_start);
        btnGPS_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permessi GPS mancanti", Toast.LENGTH_SHORT).show();
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.MIN_TIME_BETEWEEN_UPDATE, 0, locationListener);
                    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    /*
                    previusLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(previusLocation == null)
                        previusLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    */
                    previusLocation = null;
                }
                Toast.makeText(getContext(), "Inizio attività", Toast.LENGTH_SHORT).show();
                active = true;
            }
        });

        Button btnGPS_stop = (Button) getView().findViewById(R.id.button_gps_stop);
        btnGPS_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getContext(),"Fine attività",Toast.LENGTH_SHORT).show();
                active = false;
                Toast.makeText( getContext(),"GPS points: "+db.getGpsCount(),Toast.LENGTH_SHORT).show();
                Toast.makeText( getContext(),"Distanza (m): "+totalDistance,Toast.LENGTH_SHORT).show();
                locationManager.removeUpdates(locationListener);
            }
        });

        Button btnGPS_delete_all = (Button) getView().findViewById(R.id.button_gps_delete_all);
        btnGPS_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getContext(),"Cancellati tutti i GPS points ("+db.getGpsCount()+")",Toast.LENGTH_SHORT).show();
                db.deleteAllGps();
            }
        });
    }
}



