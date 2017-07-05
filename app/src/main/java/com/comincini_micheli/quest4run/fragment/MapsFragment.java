package com.comincini_micheli.quest4run.fragment;



import android.content.DialogInterface;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
//TODO quale import usare?
import android.app.AlertDialog;
//import android.support.v7.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Gps;

import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class MapsFragment extends Fragment {

    //TODO da sistemare e riordinare
    //TODO gestione automatica zoom al caricamento
    private MapView mMapView;
    private GoogleMap mMapGoogle;

    private float distance;
    private float speed;
    private long duration;

    private List<Gps> gpsList;

    public MapsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(getContext());
        gpsList = db.getAllGps();
        int startTime = 0, finishTime = 0;
        for(int i=0; i<gpsList.size(); i++){
            if(i==0) {
                startTime = gpsList.get(i).getTime();
            }
            if(i==(gpsList.size()-1)) {
                finishTime = gpsList.get(i).getTime();
            }
            if(i>0&&i<(gpsList.size()-1)){}
        }
        duration = finishTime - startTime;
        TextView textViewDistance = (TextView) view.findViewById(R.id.display_distance);
        TextView textViewSpeed = (TextView) view.findViewById(R.id.display_speed);
        Chronometer chronometer = (Chronometer) view.findViewById(R.id.display_time);

        textViewDistance.setText(String.format("%.2f",distance/ Constants.M_IN_KM));
        textViewSpeed.setText(String.format("%.1f",speed));
        String durationString = myFormatTime(duration);

        chronometer.setText(durationString);

        mMapView = (MapView) getActivity().findViewById(R.id.map3);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMapGoogle = googleMap;
                /*
                DatabaseHandler db = new DatabaseHandler(getContext());
                List<Gps> gpsList = db.getAllGps();
                */
                PolylineOptions line= new PolylineOptions().width(5).color(getResources().getColor(R.color.colorPrimary));
                LatLng point = null;
                Double meanLat = 0.0, meanLng = 0.0;

                //TODO serve counter o basta gpsList.size() ?
                int counter = 0;

                for(int i=0; i<gpsList.size(); i++){
                    point = new LatLng(
                            Double.parseDouble(
                                    gpsList.get(i).getLatitude()
                            ),
                            Double.parseDouble(
                                    gpsList.get(i).getLongitude()
                            ));
                    line.add(point);
                    meanLat += point.latitude;
                    meanLng += point.longitude;
                    counter++;

                    //TODO da vedere
                    //TODO aggiungere sempre bandierina in fondo (caso con un solo punto)
                    /*
                    if(i==0) {
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i + "").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                        startTime = gpsList.get(i).getTime();
                    }
                    else if(i==(gpsList.size()-1)) {
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i + "").anchor(0.0f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                        finishTime = gpsList.get(i).getTime();
                    }
                    else
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i+"").anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_dark_blu)));
                    */
                    if(i==0) {
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i + "").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                        //startTime = gpsList.get(i).getTime();
                    }
                    if(i==(gpsList.size()-1)) {
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i + "").anchor(0.0f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                        //finishTime = gpsList.get(i).getTime();
                    }
                    if(i>0&&i<(gpsList.size()-1))
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i+"").anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_dark_blu)));
                }

                meanLat/=counter;
                meanLng/=counter;

                mMapGoogle.addPolyline(line);
                mMapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(meanLat,meanLng),18));

                if(gpsList.isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();

                    // Setting Dialog Title
                    alertDialog.setTitle(getResources().getString(R.string.alert_no_path_title));

                    // Setting Dialog Message
                    alertDialog.setMessage(getResources().getString(R.string.alert_no_path_text));

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    // Setting OK Button
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,getResources().getString(R.string.alert_btn_positive_label), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new RunFragment());
                            fragmentTransaction.commit();
                            getActivity().setTitle(getResources().getString(R.string.nav_run));
                            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                            navigationView.setCheckedItem(R.id.nav_run);
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            }
        });
    }

    private String myFormatTime(long myDuration){
        long temp;
        String durationString="";
        temp = TimeUnit.MILLISECONDS.toHours(myDuration);
        if(temp < 10)
            durationString += "0";
        durationString += temp+":";
        myDuration -= TimeUnit.HOURS.toMillis(temp);
        temp = TimeUnit.MILLISECONDS.toMinutes(myDuration);
        if(temp < 10)
            durationString += "0";
        durationString += temp+":";
        myDuration -= TimeUnit.MINUTES.toMillis(temp);
        temp = TimeUnit.MILLISECONDS.toSeconds(myDuration);
        if(temp < 10)
            durationString += "0";
        durationString += temp;
        return durationString;
    }
}



