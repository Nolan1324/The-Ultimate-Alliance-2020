package com.nolankuza.theultimatealliance.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.room.PlayoffDataDao;
import com.nolankuza.theultimatealliance.structure.Alliance;
import com.nolankuza.theultimatealliance.structure.PlayoffData;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class AnalysisFragment extends Fragment {

    public AnalysisFragment() {

    }

    public static AnalysisFragment newInstance() {
        AnalysisFragment fragment = new AnalysisFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        update(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        update(getView());
    }

    public void update(final View view) {
        new Thread() {
            @Override
            public void run() {
                //TODO This was all rushed. Condense as soon as possible.

                PlayoffDataDao playoffDataDao = database.playoffDataDao();
                List<PlayoffData> playoffDataList = playoffDataDao.getAll();
                int index = playoffDataList.size() == 0 ? 1 : playoffDataDao.getAll().get(playoffDataList.size() - 1).index;
                final PlayoffData[] playoffDataArray = {
                        playoffDataDao.getRed1(index),
                        playoffDataDao.getRed2(index),
                        playoffDataDao.getRed3(index),
                        playoffDataDao.getBlue1(index),
                        playoffDataDao.getBlue2(index),
                        playoffDataDao.getBlue3(index)
                };
                for(int i = 0; i < playoffDataArray.length; i++) {
                    if(playoffDataArray[i] == null) playoffDataArray[i] = new PlayoffData();
                }
                Activity activity = getActivity();
                if(activity != null) {
                activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            generate(view, playoffDataArray);
                        }
                    });
                }
            }
        }.start();
    }

    private void generate(View view, PlayoffData[] playoffDataArray) {
        ((AnalysisView) view.findViewById(R.id.red_analysis)).generate(Alliance.Red,
                playoffDataArray[0],
                playoffDataArray[1],
                playoffDataArray[2]);
        ((AnalysisView) view.findViewById(R.id.blue_analysis)).generate(Alliance.Blue,
                playoffDataArray[3],
                playoffDataArray[4],
                playoffDataArray[5]);
    }

    /*
    public PlayoffData tempRandom(int teamNumber) {
        PlayoffData playoffData = new PlayoffData();
        playoffData.teamNumber = teamNumber;
        playoffData.data.hatchS = (int) (Math.random() * 12);
        playoffData.data.hatchF = (int) (Math.random() * 12);
        playoffData.data.cargoS = (int) (Math.random() * 12);
        playoffData.data.cargoF = (int) (Math.random() * 12);
        return playoffData;
    }
    */
}
