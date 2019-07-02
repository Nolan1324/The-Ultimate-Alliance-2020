package com.nolankuza.theultimatealliance.main;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.structure.Alliance;
import com.nolankuza.theultimatealliance.structure.PlayoffData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AnalysisView extends CoordinatorLayout {
    private TextView title;
    private BarChart graph;
    private TextView preload1;
    private TextView start1;
    private TextView end1;
    private TextView preload2;
    private TextView start2;
    private TextView end2;
    private TextView preload3;
    private TextView start3;
    private TextView end3;
    
    public AnalysisView(Context context) {
        super(context);
        init();
    }

    public AnalysisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnalysisView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_analysis, this);
        this.title = findViewById(R.id.title);
        this.graph = findViewById(R.id.graph);
        this.preload1 = findViewById(R.id.preload_1);
        this.start1 = findViewById(R.id.start_1);
        this.end1 = findViewById(R.id.end_1);
        this.preload2 = findViewById(R.id.preload_2);
        this.start2 = findViewById(R.id.start_2);
        this.end2 = findViewById(R.id.end_2);
        this.preload3 = findViewById(R.id.preload_3);
        this.start3 = findViewById(R.id.start_3);
        this.end3 = findViewById(R.id.end_3);
    }
    
    public void generate(Alliance alliance, PlayoffData team1, PlayoffData team2, PlayoffData team3) {
        BarDataSet successes = new BarDataSet(Arrays.asList(
                new BarEntry(0, new float[] {team1.data.hatchS, team1.data.cargoS}),
                new BarEntry(1, new float[] {team2.data.hatchS, team2.data.cargoS}),
                new BarEntry(2, new float[] {team3.data.hatchS, team3.data.cargoS})),
                "Success");
        successes.setColors(Arrays.asList(Color.parseColor("#ADFF2F"), Color.parseColor("#FFA500")));
        successes.setStackLabels(new String[] {"Hatch", "Cargo"});
        BarDataSet fails = new BarDataSet(Arrays.asList(
                new BarEntry(0, new float[] {team1.data.hatchF, team1.data.cargoF}),
                new BarEntry(1, new float[] {team2.data.hatchF, team2.data.cargoF}),
                new BarEntry(2, new float[] {team3.data.hatchF, team3.data.cargoF})),
                "Fails");
        fails.setColors(Arrays.asList(Color.parseColor("#ADFF2F"), Color.parseColor("#FFA500")));
        fails.setStackLabels(new String[] {"Hatch", "Cargo"});

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(successes);
        dataSets.add(fails);
        BarData data = new BarData(dataSets);
        data.groupBars(-0.35f, 0.2f, -0.5f);
        data.setBarWidth(0.3f);
        graph.invalidate();
        graph.setData(data);
        graph.setDescription(null);
        graph.setExtraOffsets(10, 10, 10, 10);
        graph.setPinchZoom(false);
        graph.setScaleEnabled(false);
        graph.getLegend().setEnabled(false);
        graph.getXAxis().setEnabled(true);
        graph.getXAxis().setDrawGridLines(false);
        graph.getXAxis().setTextSize(15);
        graph.getXAxis().setGranularityEnabled(true);
        graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        graph.getXAxis().setValueFormatter(new IndexAxisValueFormatter(Arrays.asList(formatTeam(team1), formatTeam(team2), formatTeam(team3))));
        graph.getAxisLeft().setAxisMaximum(24);
        graph.getAxisRight().setEnabled(false);

        title.setText(String.format("%s Alliance", alliance.toString()));
        if(team1.teamNumber != 0) {
            preload1.setText(String.format("Preload %s", getPreload(team1.data.preload)));
            start1.setText(String.format(Locale.US, "Start Level %d", team1.data.startLevel));
            end1.setText(String.format(Locale.US, "End Level %d", team1.data.endLevel));
        } else {
            preload1.setText("");
            start1.setText("");
            end1.setText("");
        }
        if(team2.teamNumber != 0) {
            preload2.setText(String.format("Preload %s", getPreload(team2.data.preload)));
            start2.setText(String.format(Locale.US, "Start Level %d", team2.data.startLevel));
            end2.setText(String.format(Locale.US, "End Level %d", team2.data.endLevel));
        } else {
            preload2.setText("");
            start2.setText("");
            end2.setText("");
        }
        if(team3.teamNumber != 0) {
            preload3.setText(String.format("Preload %s", getPreload(team3.data.preload)));
            start3.setText(String.format(Locale.US, "Start Level %d", team3.data.startLevel));
            end3.setText(String.format(Locale.US, "End Level %d", team3.data.endLevel));
        } else {
            preload3.setText("");
            start3.setText("");
            end3.setText("");
        }
    }

    private String formatTeam(PlayoffData playoffData) {
        return playoffData.teamNumber == 0 ? "" : Integer.toString(playoffData.teamNumber);
    }

    private String getPreload(int id) {
        return id == 2 ? "Cargo" : (id == 1 ? "Hatch" : "None");
    }
}