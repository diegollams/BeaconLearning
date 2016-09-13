package com.diegollams.beaconlearning.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diegollams.beaconlearning.R;
import com.estimote.sdk.Beacon;

import java.util.List;

/**
 * Created by diegollams on 9/13/16.
 */
public class BeaconsAdapter extends RecyclerView.Adapter<BeaconsAdapter.ViewHolder> {

    private List<Beacon> beaconList;

    public BeaconsAdapter(@NonNull List<Beacon> beaconList) {
        this.beaconList = beaconList;
    }

    public BeaconsAdapter() {
        this.beaconList = null;
    }

    public void setBeaconList(List<Beacon> beaconList) {
        this.beaconList = beaconList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.beacon_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.uiidTextView.setText(beaconList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        if(beaconList == null){
            return 0;
        }
        return beaconList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uiidTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            uiidTextView = (TextView) itemView.findViewById(R.id.uiid_text_view);
        }
    }
}
