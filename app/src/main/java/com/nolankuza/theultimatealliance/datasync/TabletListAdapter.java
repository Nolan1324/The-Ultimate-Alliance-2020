package com.nolankuza.theultimatealliance.datasync;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.structure.DeviceInfo;

import java.util.List;

public class TabletListAdapter extends RecyclerView.Adapter<TabletListAdapter.ViewHolder> {

    private List<DeviceInfo> devices;
    private LayoutInflater inflater;

    public TabletListAdapter(Context context, List<DeviceInfo> devices) {
        this.inflater = LayoutInflater.from(context);
        this.devices = devices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_tablet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeviceInfo device = devices.get(position);
        ToggleButton itemView = (ToggleButton) holder.itemView;
        itemView.setText(device.getName());
        itemView.setTextOn(device.getName());
        itemView.setTextOff(device.getName());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CheckBox.OnCheckedChangeListener {
        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ((ToggleButton) itemView).setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            getItem(getAdapterPosition()).toggleEnabled();
        }
    }

    public List<DeviceInfo> getDeviceInfos() {
        return devices;
    }

    public DeviceInfo getItem(int id) {
        return devices.get(id);
    }
}
