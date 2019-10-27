package com.nolankuza.theultimatealliance.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.PlayoffData;

import java.util.List;
import java.util.Locale;

public class PlayoffAdapter extends RecyclerView.Adapter<PlayoffAdapter.ViewHolder> {

    //Todo optimize scope
    private List<PlayoffData> playoffDataList;
    private int nextIndex;
    public boolean showAll;
    private final Drawable cancelDrawable;
    private final Drawable checkDrawable;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public PlayoffAdapter(Context context, List<PlayoffData> playoffDataList, int nextIndex, boolean showAll) {
        this.inflater = LayoutInflater.from(context);
        this.cancelDrawable = context.getResources().getDrawable(R.drawable.ic_cancel);
        this.checkDrawable = context.getResources().getDrawable(R.drawable.ic_check);
        this.playoffDataList = playoffDataList;
        this.nextIndex = nextIndex;
        playoffDataList.add(new PlayoffData(nextIndex));
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_playoff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayoffData playoffData = playoffDataList.get(position);
        View itemView = holder.itemView;

        if(showAll || position == 0) {
            itemView.findViewById(R.id.playoff_item_icon).setVisibility(View.VISIBLE);
            if(playoffData.scouted == 1) {
                ((ImageButton)itemView.findViewById(R.id.playoff_item_icon)).setImageDrawable(checkDrawable);
            } else {
                ((ImageButton)itemView.findViewById(R.id.playoff_item_icon)).setImageDrawable(cancelDrawable);
            }
        } else {
            itemView.findViewById(R.id.playoff_item_icon).setVisibility(View.INVISIBLE);
        }
        ((TextView)(itemView.findViewById(R.id.playoff_item_index))).setText(String.format(Locale.US, "%d", playoffData.index));
        ((TextView)(itemView.findViewById(R.id.playoff_item_team_number))).setText(playoffData.teamNumber == 0 ? "" : Integer.toString(playoffData.teamNumber));
    }

    @Override
    public int getItemCount() {
        return playoffDataList.size();
    }

    public void setData(List<PlayoffData> playoffDataList, int nextIndex) {
        this.playoffDataList = playoffDataList;
        this.nextIndex = nextIndex;
        playoffDataList.add(new PlayoffData(nextIndex));
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.playoff_item_icon).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null) {
                int position = getAdapterPosition();
                PlayoffData playoffData = getItem(position);
                if(view.getId() == R.id.playoff_item_icon) {
                    if(getItem(getAdapterPosition()).scouted == 1) {
                        clickListener.onUnscoutClick(view, playoffData);
                    } else {
                        clickListener.onRemoveClick(view, playoffData);
                    }
                } else if(showAll || position == 0) {
                    clickListener.onItemClick(view, playoffData);
                }
            }
        }
    }

    public PlayoffData getItem(int id) {
        return playoffDataList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    private PlayoffData getNextPlayoff(List<PlayoffData> playoffDataList) {
        if(playoffDataList.size() == 0) {
            return new PlayoffData(1);
        } else {
            return new PlayoffData(playoffDataList.get(playoffDataList.size() - 1).index + 1);
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, PlayoffData playoffData);
        void onRemoveClick(View view, PlayoffData playoffData);
        void onUnscoutClick(View view, PlayoffData playoffData);
    }
}
