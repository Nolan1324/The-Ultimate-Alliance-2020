package com.nolankuza.theultimatealliance.playoffs;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.model.Alliance;
import com.nolankuza.theultimatealliance.model.PlayoffData;
import com.nolankuza.theultimatealliance.scout.Counter;
import com.nolankuza.theultimatealliance.util.Prefs;

public class PlayoffActivity extends BaseActivity {

    PlayoffData playoffData = new PlayoffData();
    PlayoffData.Data data = new PlayoffData.Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_playoff);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            PlayoffData tPlayoffData = bundle.getParcelable("playoffdata");
            if(tPlayoffData != null) {
                playoffData = tPlayoffData;
                data = playoffData.data;
            }
        }
        switch(Prefs.getDriverStation("0")) {
            case "0":
                playoffData.alliance = Alliance.Red;
                playoffData.driverStation = 1;
                break;
            case "1":
                playoffData.alliance = Alliance.Red;
                playoffData.driverStation = 2;
                break;
            case "2":
                playoffData.alliance = Alliance.Red;
                playoffData.driverStation = 3;
                break;
            case "3":
                playoffData.alliance = Alliance.Blue;
                playoffData.driverStation = 1;
                break;
            case "4":
                playoffData.alliance = Alliance.Blue;
                playoffData.driverStation = 2;
                break;
            case "5":
                playoffData.alliance = Alliance.Blue;
                playoffData.driverStation = 3;
                break;
        }
        playoffData.scouter = Prefs.getStudent("Anonymous");

        actionBar.setTitle("Pit Scouting");
        actionBar.setSubtitle(playoffData.scouter + " scouting #" + playoffData.teamNumber);

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.hatchS = getValue(R.id.hatch_s);
                data.hatchF = getValue(R.id.hatch_f);
                data.cargoS = getValue(R.id.cargo_s);
                data.cargoF = getValue(R.id.cargo_f);
                data.preload = (byte) getRadioIndex(R.id.starting_item_group);
                data.startLevel = (byte) (getRadioIndex(R.id.starting_level_group) + 1);
                data.endLevel = (byte) (getRadioIndex(R.id.ending_level_group));
                
                playoffData.data = data;
                playoffData.scouted = 1;
                new SavePlayoffDataThread(playoffData, new SavePlayoffDataThread.Listener() {
                    @Override
                    public void onTaskComplete() {
                        finish();
                    }
                }).start();
            }
        });
    }
    
    private int getValue(int id) {
        return ((Counter) findViewById(id)).getValue();
    }

    private int getRadioIndex(int id) {
        RadioGroup radioGroup = findViewById(id);
        return radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
    }
}
