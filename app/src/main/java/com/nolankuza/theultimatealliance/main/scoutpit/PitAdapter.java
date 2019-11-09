package com.nolankuza.theultimatealliance.main.scoutpit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.pitdata.PitData;

import java.util.List;
import java.util.Locale;

public class PitAdapter extends RecyclerView.Adapter<PitAdapter.ViewHolder> {

    private List<PitData> pitDataList;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public PitAdapter(Context context, List<PitData> pitDataList) {
        this.inflater = LayoutInflater.from(context);
        this.pitDataList = pitDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_team_import, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PitData pitData = pitDataList.get(position);
        View itemView = holder.itemView;
        //itemView.setEnabled(false);
        ((TextView)(itemView.findViewById(R.id.team_import_item_number))).setText(String.format(Locale.US, "%d", pitData.teamNumber));
        ((TextView)(itemView.findViewById(R.id.team_import_item_name))).setText(pitData.teamName);
    }

    @Override
    public int getItemCount() {
        return pitDataList.size();
    }

    public void setData(List<PitData> pitDataList) {
        this.pitDataList = pitDataList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public PitData getItem(int id) {
        return pitDataList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
