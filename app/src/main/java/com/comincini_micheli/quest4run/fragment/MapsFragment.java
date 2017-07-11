package com.comincini_micheli.quest4run.fragment;



import android.content.Context;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Gps;

import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class MapsFragment extends Fragment {

    private GoogleMap mMapGoogle;

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

        SharedPreferences lastRunInfo = getContext().getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
        float distance = lastRunInfo.getFloat(Constants.LAST_DISTANCE, 0);
        long duration = lastRunInfo.getLong(Constants.LAST_DURATION, 0);
        float speed;
        if(duration ==0)
            speed = (float) 0.0;
        else
            speed = distance /(duration /Constants.MILLISECONDS_A_SECOND);

        TextView textViewDistance = (TextView) view.findViewById(R.id.display_distance);
        TextView textViewSpeed = (TextView) view.findViewById(R.id.display_speed);
        Chronometer chronometer = (Chronometer) view.findViewById(R.id.display_time);

        textViewDistance.setText(String.format(getResources().getString(R.string.distance_value_format), distance / Constants.M_IN_KM));
        textViewSpeed.setText(String.format(getResources().getString(R.string.speed_value_format), speed));
        String durationString = myFormatTime(duration);
        chronometer.setText(durationString);

        MapView mMapView = (MapView) getActivity().findViewById(R.id.map3);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMapGoogle = googleMap;

                DatabaseHandler db = new DatabaseHandler(getContext());
                List<Gps> gpsList = db.getAllGps();

                PolylineOptions line= new PolylineOptions().width(5).color(getResources().getColor(R.color.colorPrimary));
                LatLng point;

                if(gpsList.isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getActivity()).create();

                    // Setting Dialog Title
                    alertDialog.setTitle(getResources().getString(R.string.alert_no_path_title));

                    // Setting Dialog Message
                    alertDialog.setMessage(getResources().getString(R.string.alert_no_path_text));

                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.tick);

                    alertDialog.setCancelable(false);

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
                else {
                    for (int i = 0; i < gpsList.size(); i++) {
                        point = new LatLng(
                                Double.parseDouble(
                                        gpsList.get(i).getLatitude()
                                ),
                                Double.parseDouble(
                                        gpsList.get(i).getLongitude()
                                ));
                        line.add(point);

                        if (i == 0) {
                            mMapGoogle.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                        } else if (i == (gpsList.size() - 1)) {
                            mMapGoogle.addMarker(new MarkerOptions().position(point).anchor(0.0f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                        }
                    }

                    mMapGoogle.addPolyline(line);

                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    int width = metrics.widthPixels;
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Gps p : gpsList) {
                        LatLng pos = new LatLng(Double.parseDouble(p.getLatitude()), Double.parseDouble(p.getLongitude()));
                        builder.include(pos);
                    }

                    LatLngBounds bounds = builder.build();
                    int padding = ((width * 10) / 100); // offset from edges of the map
                    // in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                            padding);
                    mMapGoogle.moveCamera(cu);
                }
            }
        });
    }

    private String myFormatTime(long myDuration){
        long temp;
        String durationString="";
        temp = TimeUnit.MILLISECONDS.toHours(myDuration);
        if(temp>0)
        {
            if(temp < 10)
                durationString += "0";
            durationString += temp+":";
        }
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



