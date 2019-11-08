package com.nolankuza.theultimatealliance.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.pit.PitActivity;
import com.nolankuza.theultimatealliance.model.pitdata.PitData;
import com.nolankuza.theultimatealliance.tasks.PitQueryTask;

import java.util.List;

public class PitFragment extends SlaveFragment {
    PitAdapter pitAdapter;

    public PitFragment() {
        setLayout(R.layout.fragment_slave_pit);
    }

    public static PitFragment newInstance() {
        return new PitFragment();
    }

    @Override
    public void loadData(final Context context, final View view) {
        final RecyclerView pitRecycler = view.findViewById(R.id.pit_recycler);
        new PitQueryTask(false, new PitQueryTask.Listener() {
            @Override
            public void onTaskCompleted(List<PitData> pitDataList) {
                pitRecycler.setLayoutManager(new LinearLayoutManager(context));
                pitRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                pitAdapter = new PitAdapter(context.getApplicationContext(), pitDataList);
                //TODO Finish pit main implementation
                pitAdapter.setClickListener(new PitAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context.getApplicationContext(), PitActivity.class);
                        PitData pit = pitAdapter.getItem(position);
                        if (studentSpinner.getVisibility() == View.VISIBLE) {
                            pit.scouter = studentSpinner.getSelectedItem().toString();
                        }
                        intent.putExtra("pit", pit);
                        startActivity(intent);
                    }
                });
                pitRecycler.setAdapter(pitAdapter);
            }
        }).execute();
    }

    @Override
    public void updateData() {
        new PitQueryTask(showAll.isChecked(), new PitQueryTask.Listener() {
            @Override
            public void onTaskCompleted(List<PitData> pitDataList) {
                if(pitAdapter != null) {
                    pitAdapter.setData(pitDataList);
                }
            }
        }).execute();
    }
}