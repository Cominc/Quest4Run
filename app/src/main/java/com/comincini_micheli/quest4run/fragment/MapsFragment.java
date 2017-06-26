package com.comincini_micheli.quest4run.fragment;


import android.graphics.Color;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.objects.Gps;

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


public class MapsFragment extends Fragment {

    private MapView mMapView;
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
        mMapView = (MapView) getActivity().findViewById(R.id.map3);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMapGoogle = googleMap;
                DatabaseHandler db = new DatabaseHandler(getContext());
                List<Gps> gpsList = db.getAllGps();
                PolylineOptions line= new PolylineOptions().width(5).color(Color.RED);
                LatLng point = null;
                Double meanLat = 0.0, meanLng = 0.0;

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
                    if(i==0)
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i+"").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                    else if(i==(gpsList.size()-1))
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i+"").anchor(0.0f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                    else
                        mMapGoogle.addMarker(new MarkerOptions().position(point).title(i+"").anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_black)));
                }

                meanLat/=counter;
                meanLng/=counter;

                mMapGoogle.addPolyline(line);
                mMapGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(meanLat,meanLng),18));
            }
        });

    }
}



