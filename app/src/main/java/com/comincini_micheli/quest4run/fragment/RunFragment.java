package com.comincini_micheli.quest4run.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RunFragment extends Fragment {
    //TODO riattivare GPS quando i test sono finiti
    private final static String provider = LocationManager.GPS_PROVIDER;
    //private final static String provider = LocationManager.NETWORK_PROVIDER;
    private static Location previusLocation = null;
    private boolean active = false;
    private float totalDistance = 0, intermediateDistance = 0;
    private float totalSpeed;
    private long startTime, finishTime;
    private int totalGPSPoints;

    private Chronometer chronometer;

    private MapView mMapView;
    private GoogleMap mMapGoogle;
    private Marker arrowMarker = null;

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

        mMapView = (MapView) getActivity().findViewById(R.id.map2);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMapGoogle = googleMap;
            }
        });

        final Button btnGPS_start = (Button) getView().findViewById(R.id.button_gps_start);
        final Button btnGPS_stop = (Button) getView().findViewById(R.id.button_gps_stop);
        btnGPS_stop.setVisibility(View.GONE);

        final TextView textViewDistance = (TextView) getView().findViewById(R.id.display_distance);
        final TextView textViewSpeed = (TextView) getView().findViewById(R.id.display_speed);
        chronometer = (Chronometer) getView().findViewById(R.id.display_time);

        final AlertDialog alertDialogGpsOff = new AlertDialog.Builder(getActivity()).create();
        alertDialogGpsOff.setTitle(getResources().getString(R.string.alert_no_gps_title));
        alertDialogGpsOff.setMessage(getResources().getString(R.string.alert_no_gps_text));

        final ProgressDialog progressDialog= new ProgressDialog(getContext());
        progressDialog.setMessage(getResources().getString(R.string.searching_gps));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);

        final DatabaseHandler db = new DatabaseHandler(getContext());

        final LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                if(previusLocation!=null) {
                    intermediateDistance = location.distanceTo(previusLocation);
                    float actual_speed = intermediateDistance / ((location.getTime() - previusLocation.getTime()) / Constants.MILLISECONDS_A_SECOND);
                    totalSpeed += actual_speed;
                    location.setSpeed(actual_speed);
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), getResources().getString(R.string.toast_run_start), Toast.LENGTH_SHORT).show();
                    startTime = System.currentTimeMillis();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                }

                if (active) {
                    Gps newPoint = new Gps(location.getLatitude()+"", location.getLongitude()+"", (int) Math.round(location.getAltitude()), (int)location.getTime());
                    db.addGps(newPoint);
                    totalGPSPoints++;
                    totalDistance += intermediateDistance;

                    textViewDistance.setText(String.format("%.2f",totalDistance/Constants.M_IN_KM));
                    textViewSpeed.setText(String.format("%.1f",location.getSpeed()));

                    LatLng newPosition = new LatLng(location.getLatitude(),location.getLongitude());
                    PolylineOptions line= new PolylineOptions().width(5).color(getResources().getColor(R.color.colorPrimary));
                    if(previusLocation!=null) {
                        line.add(new LatLng(previusLocation.getLatitude(), previusLocation.getLongitude()));
                        //TODO aggiunta marker freccia DA TESTARE (correggere rotazione freccia)
                        //mMapGoogle.addMarker(new MarkerOptions().position(newPosition).anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromResource(R.drawable.point_dark_blu)));
                        if(arrowMarker!=null)
                            arrowMarker.remove();
                        arrowMarker = mMapGoogle.addMarker(new MarkerOptions().position(newPosition).anchor(0.5f, 0.5f).rotation(previusLocation.bearingTo(location)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_grey600_24dp)));
                    }
                    else {
                        mMapGoogle.addMarker(new MarkerOptions().position(newPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                    }
                    line.add(newPosition);
                    mMapGoogle.addPolyline(line);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(newPosition).zoom(Constants.ZOOM_LEVEL).bearing(location.getBearing()).build();
                    mMapGoogle.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                previusLocation = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Toast.makeText(getContext(), "Status change", Toast.LENGTH_SHORT).show();
            }

            public void onProviderEnabled(String provider) {
                //Toast.makeText(getContext(), "GPS is working", Toast.LENGTH_SHORT).show();
            }

            public void onProviderDisabled(String provider) {
                alertDialogGpsOff.show();
            }
        };


        // Setting Icon to Dialog
        //alertDialogGpsOff.setIcon(R.drawable.tick);
        // Setting OK Button
        alertDialogGpsOff.setButton(DialogInterface.BUTTON_POSITIVE,getResources().getString(R.string.alert_btn_positive_label), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        alertDialogGpsOff.setButton(DialogInterface.BUTTON_NEUTRAL,getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(active){
                    Toast.makeText(getContext(),getResources().getString(R.string.toast_run_gps_deactivated),Toast.LENGTH_SHORT).show();
                    active = false;
                    locationManager.removeUpdates(locationListener);
                    btnGPS_stop.setVisibility(View.GONE);
                    btnGPS_start.setVisibility(View.VISIBLE);
                    if(arrowMarker!=null)
                        arrowMarker.remove();
                    if (previusLocation != null)
                        mMapGoogle.addMarker(new MarkerOptions().position(new LatLng(previusLocation.getLatitude(), previusLocation.getLongitude())).anchor(0.0f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                    chronometer.stop();
                }
            }
        });


        btnGPS_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Constants.MY_PERMISSION_REQUEST_LOCATION);
                    else
                        Toast.makeText(getContext(), getResources().getString(R.string.toast_gps_missing_permission), Toast.LENGTH_SHORT).show();
                } else {
                    if(locationManager.isProviderEnabled(provider)) {
                        db.deleteAllGps();
                        //Toast.makeText( getContext(),"Cancellati tutti i GPS points ("+db.getGpsCount()+")",Toast.LENGTH_SHORT).show();

                        locationManager.requestLocationUpdates(provider, Constants.MIN_TIME_BETEWEEN_UPDATE, 0, locationListener);
                        progressDialog.show();

                        previusLocation = null;
                        active = true;
                        mMapGoogle.clear();
                        totalDistance = 0;
                        totalSpeed = 0;
                        totalGPSPoints = 0;
                        btnGPS_start.setVisibility(View.GONE);
                        btnGPS_stop.setVisibility(View.VISIBLE);
                    }
                    else {
                        alertDialogGpsOff.show();
                    }
                }
            }
        });

        btnGPS_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( getContext(), getResources().getString(R.string.toast_run_stop), Toast.LENGTH_SHORT).show();
                active = false;
                locationManager.removeUpdates(locationListener);
                progressDialog.dismiss();
                btnGPS_stop.setVisibility(View.GONE);
                btnGPS_start.setVisibility(View.VISIBLE);
                if(arrowMarker!=null)
                    arrowMarker.remove();
                if (previusLocation != null)
                    mMapGoogle.addMarker(new MarkerOptions().position(new LatLng(previusLocation.getLatitude(), previusLocation.getLongitude())).anchor(0.0f, 1.0f).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_finish)));
                chronometer.stop();

                //Toast.makeText( getContext(),"GPS points: "+db.getGpsCount(),Toast.LENGTH_SHORT).show();
                //Toast.makeText( getContext(),"Distanza (m): "+totalDistance,Toast.LENGTH_SHORT).show();
                finishTime = System.currentTimeMillis();
                long totalTime = (finishTime - startTime)/ Constants.MILLISECONDS_A_SECOND;
                long numberMinutes = totalTime / Constants.SECONDS_A_MINUTE;
                int totalRewardEarned = 0;
                int totalNewTaskCompleted = 0;
                if(totalDistance > 0) {

                    SharedPreferences.Editor lastRunInfo = getContext().getSharedPreferences(Constants.NAME_PREFS, getContext().MODE_PRIVATE).edit();
                    lastRunInfo.putFloat(Constants.LAST_DISTANCE, totalDistance);
                    lastRunInfo.putLong(Constants.LAST_DURATION, (finishTime - startTime));
                    lastRunInfo.commit();

                    /*if (numberMinutes > 0) {
                        Toast.makeText(getContext(), "Durata : " + numberMinutes + " minuti e " +
                                (totalTime - numberMinutes * Constants.SECONDS_A_MINUTE) + " secondi", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Durata (s): " + totalTime, Toast.LENGTH_SHORT).show();
                    }*/

                    //DISTANCE
                    List<Task> tasks_distance;
                    tasks_distance = db.getTasks(false, true, Constants.DISTANCE_TYPE_TASK);
                    for (int i = 0; i < tasks_distance.size(); i++) {
                        String goalString = getActivity().getResources().getStringArray(R.array.task_distance_goal)[tasks_distance.get(i).getGoal()];
                        double goalValue = Double.parseDouble(goalString.substring(0, goalString.length() - 3)) * Constants.M_IN_KM;
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

                        Date today = new Date(finishTime);
                        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));

                        if (sdf.format(yesterdayDate).equals(sdf.format(lastExecDate))) {
                            int progress = (int) tasks_constance.get(i).getProgress() + 1;
                            tasks_constance.get(i).setProgress(progress);
                        }else if(!sdf.format(today).equals(sdf.format(lastExecDate))){
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

                    //DURATION
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

                    SharedPreferences firstLaunchSetting = getActivity().getSharedPreferences(Constants.NAME_PREFS, Context.MODE_PRIVATE);
                    int characterId = firstLaunchSetting.getInt(Constants.CHAR_ID_PREFERENCE, -1);
                    Character myCharacter = db.getCharacter(characterId);
                    myCharacter.setWallet(myCharacter.getWallet() + totalRewardEarned);
                    db.updateCharacter(myCharacter);
                    Toast.makeText(getContext(), String.format(getResources().getString(R.string.toast_money_earned), totalRewardEarned), Toast.LENGTH_SHORT).show();

                    if (totalNewTaskCompleted > 0) {
                        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent notifyIntent = new Intent(getActivity(), TaskHistoryActivity.class);
                        PendingIntent intent = PendingIntent.getActivity(getActivity(), 0, notifyIntent, 0);

                        final Notification notifyDetails = new Notification.Builder(getContext())
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.notification)
                                .setWhen(System.currentTimeMillis())
                                .setContentText(String.format(getResources().getString(R.string.notification_message_run_reward),totalRewardEarned))
                                .setContentTitle(String.format(getResources().getString(R.string.notification_title_run_reward),totalNewTaskCompleted))
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setContentIntent(intent)
                                .build();
                        notifyDetails.flags = notifyDetails.flags | Notification.FLAG_AUTO_CANCEL;
                        nm.notify((int)((new Date().getTime()/1000L)%Integer.MAX_VALUE), notifyDetails);
                    }
                }
                else
                {
                    db.deleteAllGps();
                    SharedPreferences.Editor lastRunInfo = getContext().getSharedPreferences(Constants.NAME_PREFS, getContext().MODE_PRIVATE).edit();
                    lastRunInfo.remove(Constants.LAST_DISTANCE);
                    lastRunInfo.remove(Constants.LAST_DURATION);
                    lastRunInfo.commit();
                    Toast.makeText(getContext(),R.string.warning_zero_distance, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}



