package com.nolankuza.theultimatealliance.scout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Match;
import com.nolankuza.theultimatealliance.model.gamedata.GameData;
import com.nolankuza.theultimatealliance.util.Prefs;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class ScoutBasicActivity extends BaseActivity implements GameDataListener {

    Match match = new Match();
    GameData gameData = new GameData();
    public GameData.Data data = new GameData.Data();

    private AutoFragment autoFragment;
    private AutoScoreFragment autoScoreFragment;
    private TeleopFragment teleopFragment;
    private EndGameFragment endGameFragment;

    private ViewPager pager;
    private boolean scouted;
    private boolean startedAutoScoreTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scout_basic);
        super.onCreate(savedInstanceState);

        actionBar.setTitle("Auto");

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Match tMatch = bundle.getParcelable("match");
            if(tMatch != null) {
                match = tMatch;
            }
        }

        scouted = match.scouted;
        if(scouted) {
            new Thread() {
                @Override
                public void run() {
                    gameData = database.gameDataDao().get(match.key);
                    data = gameData.data;
                    updateActionBar();
                }
            }.start();
        } else {
            gameData = GameData.fromMatch(match);
            updateActionBar();
        }

        pager = findViewById(R.id.pager);
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position) {
                    case 0:
                        autoFragment = AutoFragment.newInstance();
                        return autoFragment;
                    /*case 1:
                        autoScoreFragment = AutoScoreFragment.newInstance();
                        return autoScoreFragment;*/
                    case 1:
                        teleopFragment = TeleopFragment.newInstance();
                        return teleopFragment;
                    case 2:
                        endGameFragment = EndGameFragment.newInstance();
                        return endGameFragment;
                    default:
                        autoFragment = AutoFragment.newInstance();
                        return autoFragment;
                }
            }

            @Override
            public int getCount() {
                return 3/*4*/;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch(position) {
                    case 0:
                        return "Auto";
                    /*case 1:
                        return "Auto";*/
                    case 1:
                        return "TeleOp";
                    case 2:
                        return "End Game";
                    default:
                        return "Auto";
                }
            }
        };
        pager.setOffscreenPageLimit(2/*3*/);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setTitle(adapter.getPageTitle(position));
                /*if(position == 1 && !startedAutoScoreTimer && autoScoreFragment != null) {
                    autoScoreFragment.startTimer();
                    startedAutoScoreTimer = true;
                }*/
                Prefs.setCurrentScoutingPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // TODO Implement
        /*
        int startingPage = prefs.getInt(Constants.PREF_CURRENT_SCOUTING_PAGE, -1);
        if(startingPage >= 0 && startingPage <= 3) {
            pager.setCurrentItem(startingPage);
        }
        */
        //TabLayout tabs = findViewById(R.id.tabs);
        //tabs.setupWithViewPager(pager);
        Prefs.setCurrentScoutingPage(pager.getCurrentItem());
        Prefs.setNextMatch(gameData.matchKey);
    }

    GameData getGameData() {
        return gameData;
    }

    public void autoScoring() {
        pager.setCurrentItem(1);
    }

    public boolean isScouted() {
        return scouted;
    }

    @Override
    public void onDataUpdated(GameData.Data data) {
        this.data = data;
        gameData.data = data;
    }

    @Override
    public void onSave() {
        //TODO autoFragment.save();
        autoScoreFragment.save();
        teleopFragment.save();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING");
        builder.setMessage("Are you sure you want to SAVE and EXIT?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                gameData.data = data;
                gameData.scouted = 1;
                //prefs.edit().putInt(Constants.PREF_NEXT_MATCH, gameData.matchNumber + 1).apply();
                Prefs.setCurrentScoutingPage(-1);
                new SaveScoutingDataThread(gameData, new SaveScoutingDataThread.Listener() {
                    @Override
                    public void onTaskComplete() {
                        finish();
                    }
                }).start();
            }
        });
        builder.setNegativeButton("NO", null);
        builder.create().show();
    }

    @Override
    public void enableScrolling(boolean enable) {
        pager.requestDisallowInterceptTouchEvent(!enable);
    }

    @Override
    public void displayDefense(boolean display) {
        endGameFragment.displayDefense(display);
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }

    private void updateActionBar() {
        actionBar.setSubtitle(gameData.scouter + " scouting " + gameData.teamNumber);
    }
}
