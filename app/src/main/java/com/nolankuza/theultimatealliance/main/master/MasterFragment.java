package com.nolankuza.theultimatealliance.main.master;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nolankuza.theultimatealliance.ApplicationState;
import com.nolankuza.theultimatealliance.R;

import static com.nolankuza.theultimatealliance.ApplicationState.prefs;

public class MasterFragment extends Fragment implements MasterListener {

    private MasterSetupFragment setupFragment;
    private MasterPitFragment pitFragment;
    private MasterMatchFragment matchFragment;

    private ViewPager pager;
    private FragmentPagerAdapter adapter;

    public boolean initPage = false;

    public MasterFragment() {

    }

    public static MasterFragment newInstance() {
        MasterFragment fragment = new MasterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_master, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pager = view.findViewById(R.id.pager);
        final TabLayout tabs = view.findViewById(R.id.tabs);
        adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position) {
                    case 0:
                        setupFragment = MasterSetupFragment.newInstance();
                        return setupFragment;
                    case 1:
                        pitFragment = MasterPitFragment.newInstance();
                        return pitFragment;
                    case 2:
                        matchFragment = MasterMatchFragment.newInstance();
                        return matchFragment;
                    default:
                        setupFragment = MasterSetupFragment.newInstance();
                        return setupFragment;
                }
            }

            @Override
            public int getCount() {
                return ApplicationState.masterCount;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position) {
                    case 0:
                        return "Setup";
                    case 1:
                        return "Pits";
                    case 2:
                        return "Matches";
                    default:
                        return "Setup";
                }
            }
        };
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                prefs.edit().putInt("master_page", position).apply();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setupWithViewPager(pager);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setupDone() {
        ApplicationState.masterCount = 3;
        adapter.notifyDataSetChanged();
        if(adapter.getCount() == 3 && !initPage) {
            pager.setCurrentItem(prefs.getInt("master_page", 0));
        }
        initPage = true;
        //pager.setCurrentItem(1);
    }
}