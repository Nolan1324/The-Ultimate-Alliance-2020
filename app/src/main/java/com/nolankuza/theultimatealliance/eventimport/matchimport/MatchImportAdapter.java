package com.nolankuza.theultimatealliance.eventimport.matchimport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.structure.Match;

import java.util.List;
import java.util.Locale;

public class MatchImportAdapter extends RecyclerView.Adapter<MatchImportAdapter.ViewHolder> {

    private List<Match> matches;
    public boolean showAll;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public MatchImportAdapter(Context context, List<Match> matches, boolean showAll) {
        this.inflater = LayoutInflater.from(context);
        this.matches = matches;
        this.showAll = showAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_match_import, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Match match = matches.get(position);
        View itemView = holder.itemView;

        if(showAll || position == 0) {
            itemView.findViewById(R.id.match_import_item_disabled).setVisibility(View.GONE);
        }
        ((TextView)(itemView.findViewById(R.id.match_import_item_date))).setText(match.time == 0 ? "N/A" : match.getTimeUS());
        ((TextView)(itemView.findViewById(R.id.match_import_item_number))).setText(String.format(Locale.US, "%d", match.matchNumber));
        ((TextView)(itemView.findViewById(R.id.match_import_item_red1))).setText(match.red1);
        ((TextView)(itemView.findViewById(R.id.match_import_item_red2))).setText(match.red2);
        ((TextView)(itemView.findViewById(R.id.match_import_item_red3))).setText(match.red3);
        ((TextView)(itemView.findViewById(R.id.match_import_item_blue1))).setText(match.blue1);
        ((TextView)(itemView.findViewById(R.id.match_import_item_blue2))).setText(match.blue2);
        ((TextView)(itemView.findViewById(R.id.match_import_item_blue3))).setText(match.blue3);
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
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null && (showAll || getAdapterPosition() == 0)) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Match getItem(int id) {
        return matches.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
