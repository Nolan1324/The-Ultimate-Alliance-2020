package com.nolankuza.theultimatealliance.main.scoutmatch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Match;

import java.util.List;
import java.util.Locale;

public class ScoutAdapter extends RecyclerView.Adapter<ScoutAdapter.ViewHolder> {

    private List<Match> matches;
    public boolean showAll;
    private final Drawable cancelDrawable;
    private final Drawable checkDrawable;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public ScoutAdapter(Context context, List<Match> matches, boolean showAll) {
        final int cancelId = R.drawable.ic_cancel;
        final int checkId = R.drawable.ic_check;

        this.inflater = LayoutInflater.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.cancelDrawable = context.getResources().getDrawable(cancelId, context.getTheme());
            this.checkDrawable = context.getResources().getDrawable(checkId, context.getTheme());
        } else {
            this.cancelDrawable = context.getResources().getDrawable(cancelId);
            this.checkDrawable = context.getResources().getDrawable(checkId);
        }
        this.matches = matches;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_match, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Match match = matches.get(position);
        View itemView = holder.itemView;

        if(showAll || position == 0) {
            itemView.findViewById(R.id.match_item_disabled).setVisibility(View.GONE);
            itemView.findViewById(R.id.match_item_icon).setVisibility(View.VISIBLE);
            if(match.scouted) {
                ((ImageButton)itemView.findViewById(R.id.match_item_icon)).setImageDrawable(checkDrawable);
            } else {
                ((ImageButton)itemView.findViewById(R.id.match_item_icon)).setImageDrawable(cancelDrawable);
            }
        } else {
            itemView.findViewById(R.id.match_item_disabled).setVisibility(View.VISIBLE);
            itemView.findViewById(R.id.match_item_icon).setVisibility(View.INVISIBLE);
        }
        ((TextView)(itemView.findViewById(R.id.match_item_date))).setText(match.time == 0 ? "N/A" : match.getTimeUS());
        ((TextView)(itemView.findViewById(R.id.match_item_number))).setText(String.format(Locale.US, "%d", match.matchNumber));
        ((TextView)(itemView.findViewById(R.id.match_item_red1))).setText(match.red1);
        ((TextView)(itemView.findViewById(R.id.match_item_red2))).setText(match.red2);
        ((TextView)(itemView.findViewById(R.id.match_item_red3))).setText(match.red3);
        ((TextView)(itemView.findViewById(R.id.match_item_blue1))).setText(match.blue1);
        ((TextView)(itemView.findViewById(R.id.match_item_blue2))).setText(match.blue2);
        ((TextView)(itemView.findViewById(R.id.match_item_blue3))).setText(match.blue3);
    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    public void setData(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.match_item_icon).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null) {
                int position = getAdapterPosition();
                Match match = getItem(position);
                if(view.getId() == R.id.match_item_icon) {
                    if(getItem(getAdapterPosition()).scouted) {
                        clickListener.onUnscoutClick(view, match);
                    } else {
                        clickListener.onRemoveClick(view, match);
                    }
                } else if(showAll || position == 0) {
                    clickListener.onItemClick(view, match);
                }
            }
        }
    }

    public Match getItem(int id) {
        return matches.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, Match match);
        void onRemoveClick(View view, Match match);
        void onUnscoutClick(View view, Match match);
    }
}
