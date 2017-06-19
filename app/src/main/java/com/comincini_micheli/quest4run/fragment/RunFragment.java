package com.comincini_micheli.quest4run.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
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


public class RunFragment extends Fragment {

    static Location previusLocation = null;

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
        final TextView textViewLocation = (TextView) getView().findViewById(R.id.textview_location);

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        previusLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if(previusLocation != null) {
            textViewLocation.setText("Latitudine: " + previusLocation.getLatitude() + "\n" +
                    "Longitudine: " + previusLocation.getLongitude() + "\n" +
                    "Altitudine: " + previusLocation.getAltitude() + "\n" +
                    "Velocità: " + previusLocation.getSpeed() + " (" + previusLocation.hasSpeed() + ")\n" +
                    "Pendenza: " + previusLocation.getBearing() + " (" + previusLocation.hasBearing() + ")");
        }
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Toast.makeText(getContext(), "Location changed!", Toast.LENGTH_SHORT).show();
                location.setSpeed(location.distanceTo(previusLocation)/((previusLocation.getTime()-location.getTime())/1000));
                textViewLocation.setText("Latitudine: "+ location.getLatitude()+"\n"+
                        "Longitudine: "+location.getLongitude()+"\n"+
                        "Altitudine: "+location.getAltitude()+"\n"+
                        "Velocità: "+location.getSpeed()+" ("+location.hasSpeed()+")\n"+
                        "Pendenza: "+location.getBearing()+" ("+location.hasBearing()+")");

                previusLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText( getContext(),"Status change",Toast.LENGTH_SHORT).show();
            }

            public void onProviderEnabled(String provider) {
                Toast.makeText( getContext(),"GPS is working",Toast.LENGTH_SHORT).show();
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText( getContext(),"GPS is not working", Toast.LENGTH_SHORT ).show();
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permessi GPS mancanti", Toast.LENGTH_SHORT).show();
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }


        Button btnGPS = (Button) getView().findViewById(R.id.button_gps);
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView t = (TextView) getActivity().findViewById(R.id.textview_location);
                t.setText("ciao");
            }
        });
    }
}



