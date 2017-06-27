package com.comincini_micheli.quest4run.fragment;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comincini_micheli.quest4run.R;
import com.comincini_micheli.quest4run.activity.TaskHistoryActivity;
import com.comincini_micheli.quest4run.objects.Character;
import com.comincini_micheli.quest4run.objects.Gps;
import com.comincini_micheli.quest4run.objects.Task;
import com.comincini_micheli.quest4run.other.Constants;
import com.comincini_micheli.quest4run.other.DatabaseHandler;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RunFragment extends Fragment {

    //TODO riattivare GPS quando i test sono finiti
    final static String provider = LocationManager.GPS_PROVIDER;
    //final static String provider = LocationManager.NETWORK_PROVIDER;
    static Location previusLocation = null;
    boolean active = false;
    float totalDistance = 0, intermediateDistance = 0;
    float totalSpeed;
    int totalGPSPoints;

    long startTime, finishTime;

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
        final Button btnGPS_start = (Button) getView().findViewById(R.id.button_gps_start);
        final Button btnGPS_stop = (Button) getView().findViewById(R.id.button_gps_stop);
        btnGPS_stop.setVisibility(View.GONE);

        final TextView textViewLocation = (TextView) getView().findViewById(R.id.textview_location);
        final DatabaseHandler db = new DatabaseHandler(getContext());

        final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Toast.makeText(getContext(), "Location changed!", Toast.LENGTH_SHORT).show();
                if(previusLocation!=null) {
                    intermediateDistance = location.distanceTo(previusLocation);
                    float actual_speed = intermediateDistance / ((location.getTime() - previusLocation.getTime()) / 1000);
                    totalSpeed += actual_speed;
                    location.setSpeed(actual_speed);
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
                    totalGPSPoints++;
                    totalDistance += intermediateDistance;

                    LatLng newPoisition = new LatLng(location.getLatitude(),location.getLongitude());
                    PolylineOptions line= new PolylineOptions().width(5).color(Color.RED);
                    if(previusLocation!=null) {
                        line.add(new LatLng(previusLocation.getLatitude(), previusLocation.getLongitude()));
                        mMapGoogle.addMarker(new MarkerOptions().position(newPoisition).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_black)));
                    }
                    else {
                        mMapGoogle.addMarker(new MarkerOptions().position(newPoisition).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                    }
                    line.add(newPoisition);
                    mMapGoogle.addPolyline(line);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(newPoisition).zoom(18).bearing(location.getBearing()).build();
                    mMapGoogle.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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


        btnGPS_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllGps();
                Toast.makeText( getContext(),"Cancellati tutti i GPS points ("+db.getGpsCount()+")",Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permessi GPS mancanti", Toast.LENGTH_SHORT).show();
                } else {
                    if(locationManager.isProviderEnabled(provider)) {
                        locationManager.requestLocationUpdates(provider, Constants.MIN_TIME_BETEWEEN_UPDATE, 0, locationListener);

                        previusLocation = null;
                        Toast.makeText(getContext(), "Inizio attività", Toast.LENGTH_SHORT).show();
                        active = true;
                        btnGPS_start.setVisibility(View.GONE);
                        btnGPS_stop.setVisibility(View.VISIBLE);
                        startTime = System.currentTimeMillis();
                    }
                    else {
                        Toast.makeText(getContext(), "GPS is not working", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnGPS_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getContext(),"Fine attività",Toast.LENGTH_SHORT).show();
                active = false;
                locationManager.removeUpdates(locationListener);
                btnGPS_stop.setVisibility(View.GONE);
                btnGPS_start.setVisibility(View.VISIBLE);
                if (previusLocation != null)
                    mMapGoogle.addMarker(new MarkerOptions().position(new LatLng(previusLocation.getLatitude(), previusLocation.getLongitude())).anchor(0.0f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                Toast.makeText( getContext(),"GPS points: "+db.getGpsCount(),Toast.LENGTH_SHORT).show();
                Toast.makeText( getContext(),"Distanza (m): "+totalDistance,Toast.LENGTH_SHORT).show();

                finishTime = System.currentTimeMillis();
                long totalTime = (finishTime - startTime)/ Constants.MILLISECONDS_A_SECOND;
                long numberMinutes = totalTime / Constants.SECONDS_A_MINUTE;
                int totalRewardEarned = 0;
                int totalNewTaskCompleted = 0;
                if(totalDistance > 0) {
                    if (numberMinutes > 0) {
                        Toast.makeText(getContext(), "Durata : " + numberMinutes * Constants.SECONDS_A_MINUTE + " minuti e " +
                                (totalTime - numberMinutes * Constants.SECONDS_A_MINUTE) + " secondi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Durata (s): " + totalTime, Toast.LENGTH_SHORT).show();
                    }

                    //DISTANCE
                    List<Task> tasks_distance;
                    tasks_distance = db.getTasks(false, true, Constants.DISTANCE_TYPE_TASK);
                    for (int i = 0; i < tasks_distance.size(); i++) {
                        String goalString = getActivity().getResources().getStringArray(R.array.task_distance_goal)[tasks_distance.get(i).getGoal()];
                        double goalValue = Double.parseDouble(goalString.substring(0, goalString.length() - 3)) * Constants.FROM_KM_TO_M;
                        if ((tasks_distance.get(i).getProgress() + totalDistance) > goalValue) {
                            tasks_distance.get(i).setCompleted(true);
                            String rewardString = getActivity().getResources().getStringArray(R.array.task_distance_reward)[tasks_distance.get(i).getReward()];
                            int rewardValue = Integer.parseInt(rewardString.substring(0, rewardString.length() - 2));
                            totalRewardEarned += rewardValue;
                            totalNewTaskCompleted++;
                        } else
                            tasks_distance.get(i).setProgress(tasks_distance.get(i).getProgress() + totalDistance);

                        tasks_distance.get(i).setExecDate(finishTime);
                        db.updateTask(tasks_distance.get(i));
                    }

                    //RITHM
                    List<Task> tasks_rithm;
                    tasks_rithm = db.getTasks(false, true, Constants.PACE_TYPE_TASK);
                    for (int i = 0; i < tasks_rithm.size(); i++) {
                        String goalString = getActivity().getResources().getStringArray(R.array.task_rithm_goal)[tasks_rithm.get(i).getGoal()];
                        double goalValue = Double.parseDouble(goalString.substring(0, goalString.length() - 4));
                        if (goalValue <= totalSpeed / totalGPSPoints) {
                            tasks_rithm.get(i).setCompleted(true);
                            tasks_rithm.get(i).setExecDate(finishTime);
                            String rewardString = getActivity().getResources().getStringArray(R.array.task_rithm_reward)[tasks_rithm.get(i).getReward()];
                            int rewardValue = Integer.parseInt(rewardString.substring(0, rewardString.length() - 2));
                            totalRewardEarned += rewardValue;
                            totalNewTaskCompleted++;
                        }

                        db.updateTask(tasks_rithm.get(i));
                    }

                    //CONSTANCE
                    List<Task> tasks_constance;
                    tasks_constance = db.getTasks(false, true, Constants.CONSTANCE_TYPE_TASK);
                    for (int i = 0; i < tasks_constance.size(); i++) {
                        String goalString = getActivity().getResources().getStringArray(R.array.task_constance_goal)[tasks_constance.get(i).getGoal()];
                        int goalValue = Integer.parseInt(goalString.substring(0, goalString.length() - 7));

                        Date lastExecDate = new Date(tasks_constance.get(i).getExecDate());
                        Date yesterdayDate = new Date(finishTime - Constants.MILLISECONDS_A_DAY);

                        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));

                        if (sdf.format(yesterdayDate).equals(sdf.format(lastExecDate))) {
                            int progress = (int) tasks_constance.get(i).getProgress() + 1;
                            tasks_constance.get(i).setProgress(progress);
                        } else {
                            tasks_constance.get(i).setProgress(1);
                        }
                        if (tasks_constance.get(i).getProgress() >= goalValue) {
                            tasks_constance.get(i).setCompleted(true);
                            String rewardString = getActivity().getResources().getStringArray(R.array.task_constance_reward)[tasks_constance.get(i).getReward()];
                            int rewardValue = Integer.parseInt(rewardString.substring(0, rewardString.length() - 2));
                            totalRewardEarned += rewardValue;
                            totalNewTaskCompleted++;
                        }
                        tasks_constance.get(i).setExecDate(finishTime);
                        db.updateTask(tasks_constance.get(i));
                    }

                    List<Task> tasks_duration;
                    tasks_duration = db.getTasks(false, true, Constants.DURATION_TYPE_TASK);
                    for (int i = 0; i < tasks_duration.size(); i++) {
                        String goalString = getActivity().getResources().getStringArray(R.array.task_duration_goal)[tasks_duration.get(i).getGoal()];
                        int goalValue = Integer.parseInt(goalString.substring(0, goalString.length() - 7)) * Constants.SECONDS_A_MINUTE;
                        if (goalValue <= totalTime) {
                            tasks_duration.get(i).setCompleted(true);
                            tasks_duration.get(i).setExecDate(finishTime);
                            String rewardString = getActivity().getResources().getStringArray(R.array.task_duration_reward)[tasks_duration.get(i).getReward()];
                            int rewardValue = Integer.parseInt(rewardString.substring(0, rewardString.length() - 2));
                            totalRewardEarned += rewardValue;
                            totalNewTaskCompleted++;
                        }

                        db.updateTask(tasks_duration.get(i));
                    }

                    //TODO non da errore sia con getActivity sia con getContext ???
                    SharedPreferences firstLaunchSetting = getActivity().getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
                    int characterId = firstLaunchSetting.getInt(Constants.CHAR_ID_PREFERENCE, -1);
                    Character myCharacter = db.getCharacter(characterId);
                    myCharacter.setWallet(myCharacter.getWallet() + totalRewardEarned);
                    db.updateCharacter(myCharacter);
                    Toast.makeText(getContext(), "Monete guadagnate: " + totalRewardEarned + getResources().getString(R.string.cents_symbol_label), Toast.LENGTH_SHORT).show();

                    if (totalNewTaskCompleted > 0) {
                        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent notifyIntent = new Intent(getActivity(), TaskHistoryActivity.class);
                        PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, notifyIntent, 0);
                        final Notification notifyDetails = new Notification.Builder(getContext())
                                .setTicker("Ticker")
                                //.setSmallIcon(R.mipmap.ic_test)
                                .setSmallIcon(R.drawable.splash)
                                .setWhen(System.currentTimeMillis())
                                .setContentText("Hai guadagnato " + totalRewardEarned + getResources().getString(R.string.cents_symbol_label))
                                .setContentTitle("Hai completato " + totalNewTaskCompleted + " task!")
                                .setContentIntent(intent)
                                .build();
                        notifyDetails.flags = notifyDetails.flags | Notification.FLAG_AUTO_CANCEL;
                        //TODO 1 = SIMPLE_NOTIFICATION_ID
                        nm.notify(1, notifyDetails);
                    }
                }
            }
        });
    }
}



