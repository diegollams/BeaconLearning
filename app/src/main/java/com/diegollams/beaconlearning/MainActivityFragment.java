package com.diegollams.beaconlearning;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private TextView textView;
    private TextView textView2;

    private BeaconManager beaconManager;
    private Region region;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textView = (TextView) view.findViewById(R.id.textview);
        textView2 = (TextView) view.findViewById(R.id.textView2);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        beaconManager.stopMonitoring(region);
        beaconManager.disconnect();
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
        defineRegion();
        beaconManager.setMonitoringListener(monitoringListener);
        beaconManager.connect(onConnectBeaconCallback);
    }

    //class methods

    private void defineRegion(){
        region = new Region(
                "monitoring"
                , UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
                , 24209, 65369
        );
    }

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


    //Listeners
    BeaconManager.MonitoringListener monitoringListener  = new BeaconManager.MonitoringListener() {
        @Override
        public void onEnteredRegion(Region region, List<Beacon> list) {
            String message ="list : " +list.size()  + "ident " + region.getIdentifier() + "major " + region.getMajor() + " " + region.getProximityUUID();
            showNotification("Beacon", "Enter Region", 1);
            textView.setText(message);
            textView2.setText(message);
        }

        @Override
        public void onExitedRegion(Region region) {
            String message = "ident " + region.getIdentifier() + " major " + region.getMajor() + " " + region.getProximityUUID();
            showNotification("Beacon", "Exit region", 1);
            textView.setText(message);

        }
    };


    BeaconManager.ServiceReadyCallback onConnectBeaconCallback = new BeaconManager.ServiceReadyCallback() {
        @Override
        public void onServiceReady() {
            textView.setText("ready");
            showNotification("~Beacon", "Ready", 2);
            beaconManager.startMonitoring(region);
        }
    };
}
