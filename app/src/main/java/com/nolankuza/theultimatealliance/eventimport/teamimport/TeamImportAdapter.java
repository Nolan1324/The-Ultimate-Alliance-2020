package com.nolankuza.theultimatealliance.eventimport.teamimport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Team;

import java.util.List;
import java.util.Locale;

public class TeamImportAdapter extends RecyclerView.Adapter<TeamImportAdapter.ViewHolder> {

    private List<Team> teams;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    public TeamImportAdapter(Context context, List<Team> teams) {
        this.inflater = LayoutInflater.from(context);
        this.teams = teams;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_team_import, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team team = teams.get(position);
        View itemView = holder.itemView;

        ((TextView)(itemView.findViewById(R.id.team_import_item_number))).setText(String.format(Locale.US, "%d", team.teamNumber));
        ((TextView)(itemView.findViewById(R.id.team_import_item_name))).setText(team.nickname);
    }

    @Override
    public int getItemCount() {
        return teams.size();
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

    /*
    public Match getItem(int id) {
        return matches.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
    */

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
