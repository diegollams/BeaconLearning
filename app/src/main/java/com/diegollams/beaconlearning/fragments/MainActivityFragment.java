package com.diegollams.beaconlearning.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegollams.beaconlearning.R;
import com.diegollams.beaconlearning.activities.MainActivity;
import com.diegollams.beaconlearning.adapters.BeaconsAdapter;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.List;
import java.util.UUID;

/**
 * asdas
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView beaconRecyclerView;
    private BeaconsAdapter beaconsAdapter;
    private FloatingActionButton stopButton;

    private BeaconManager beaconManager;
    private static final Region region = new Region("monitoring", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);
    private boolean isRanging;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initComponents(view);

        return view;
    }

    private void initBeaconRecycle(View view) {
        beaconRecyclerView = (RecyclerView) view.findViewById(R.id.beacon_recycle_view);
        beaconsAdapter = new BeaconsAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        beaconRecyclerView.setLayoutManager(mLayoutManager);
        beaconRecyclerView.setItemAnimator(new DefaultItemAnimator());
        beaconRecyclerView.setAdapter(beaconsAdapter);
    }

    private void initComponents(View view) {
        stopButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if(stopButton != null){
            //TODO this should't be null
            stopButton.setOnClickListener(stopButtonListener);
        }
        initBeaconRecycle(view);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRanging();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());
        beaconManager = new BeaconManager(getContext());
        beaconManager.setRangingListener(rangingListener);
        beaconManager.connect(onConnectBeaconCallback);
    }

    //class methods


    private void showNotification(String title, String message, int id){
        Intent showNotification = new Intent(getContext(), MainActivity.class);
        showNotification.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(getContext(), 0 , new Intent[]{showNotification}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(getContext())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    private void startRanging(){
        isRanging = true;
        beaconManager.startRanging(region);
    }

    private void stopRanging(){
        isRanging = false;
        beaconManager.stopRanging(region);
    }


    //Listeners
    private View.OnClickListener stopButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isRanging){
                stopRanging();
            }else{
                startRanging();
            }
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    };

    private BeaconManager.RangingListener rangingListener = new BeaconManager.RangingListener() {
        @Override
        public void onBeaconsDiscovered(Region region, List<Beacon> list) {
            beaconsAdapter.setBeaconList(list);
        }
    };


    private BeaconManager.ServiceReadyCallback onConnectBeaconCallback = new BeaconManager.ServiceReadyCallback() {
        @Override
        public void onServiceReady() {
        startRanging();
        }
    };
}
