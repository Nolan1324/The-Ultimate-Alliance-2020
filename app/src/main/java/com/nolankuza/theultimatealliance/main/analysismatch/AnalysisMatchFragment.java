package com.nolankuza.theultimatealliance.main.analysismatch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nolankuza.theultimatealliance.R;

public class AnalysisMatchFragment extends Fragment {

    public AnalysisMatchFragment() {

    }

    public static AnalysisMatchFragment newInstance() {
        AnalysisMatchFragment fragment = new AnalysisMatchFragment();
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
        return inflater.inflate(R.layout.fragment_analysis_playoff, container, false);
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
                /*

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
                */
            }
        }.start();
    }
}
