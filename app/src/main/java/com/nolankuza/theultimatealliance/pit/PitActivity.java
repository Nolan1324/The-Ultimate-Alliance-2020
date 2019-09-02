package com.nolankuza.theultimatealliance.pit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.structure.PitData;
import com.nolankuza.theultimatealliance.util.DataUtil;

public class PitActivity extends BaseActivity {

    PitData pitData = new PitData();
    PitData.Data data = new PitData.Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pit);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            PitData tPitData = bundle.getParcelable("pit");
            if(tPitData != null) {
                pitData = tPitData;
                data = pitData.data;
            }
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        pitData.scouter = prefs.getString("student_pref", "Anonymous");

        actionBar.setTitle("Pit Scouting");
        actionBar.setSubtitle(pitData.scouter + " scouting #" + pitData.teamNumber + " " + pitData.teamName);

        findViewById(R.id.pit_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.length = getFloat(R.id.length);
                data.width = getFloat(R.id.width);
                data.height = getFloat(R.id.height);
                data.weight = getFloat(R.id.weight);
                data.topSpeedFps = getFloat(R.id.speed_fps);
                data.language = getOption(R.id.language);
                data.numWheels = getOption(R.id.num_wheels);
                data.wheelType = getOption(R.id.wheel_type);
                data.numSecondaryWheels = getOption(R.id.num_secondary_wheels);
                data.secondaryWheelType = getOption(R.id.secondary_wheel_type);
                data.driveTrainType = getOption(R.id.drive_train_type);
                data.driveMotorType = getOption(R.id.drive_motor_type);
                data.numDriveMotors = getOption(R.id.num_drive_motors);
                data.startingPositionL = getChecked(R.id.starting_position_l);
                data.startingPositionC = getChecked(R.id.starting_position_c);
                data.startingPositionR = getChecked(R.id.starting_position_r);
                data.startingLevel1 = getChecked(R.id.starting_level_1);
                data.startingLevel2 = getChecked(R.id.starting_level_2);
                data.autoLeaveHab = getYN(R.id.auto_leave_hab);
                data.autoShipL = getChecked(R.id.auto_ship_l);
                data.autoShipC = getChecked(R.id.auto_ship_c);
                data.autoShipR = getChecked(R.id.auto_ship_r);
                data.autoRocketL = getChecked(R.id.auto_rocket_l);
                data.autoRocketR = getChecked(R.id.auto_rocket_r);
                data.retractFrame = getYN(R.id.retract_frame);
                data.defense = getOption(R.id.can_play_defense);
                data.climbLevel1 = getChecked(R.id.climb_level_1);
                data.climbLevel2 = getChecked(R.id.climb_level_2);
                data.climbLevel3 = getChecked(R.id.climb_level_3);
                data.climbTime = getFloat(R.id.climb_time);
                data.assistToLevel2 = getOption(R.id.assist_to_level_2);
                data.assistToLevel3 = getOption(R.id.assist_to_level_3);
                data.hatchFromStation = getChecked(R.id.hatch_from_station);
                data.hatchFromFloor = getChecked(R.id.hatch_from_floor);
                data.hatchToShip = getChecked(R.id.hatch_to_ship);
                data.hatchToRocket1 = getChecked(R.id.hatch_to_rocket_1);
                data.hatchToRocket2 = getChecked(R.id.hatch_to_rocket_2);
                data.hatchToRocket3 = getChecked(R.id.hatch_to_rocket_3);
                data.hatchFromOpponentSide = getYN(R.id.hatch_from_opponent_side);
                data.cargoFromDepot = getChecked(R.id.cargo_from_depot);
                data.cargoFromStation = getChecked(R.id.cargo_from_floor);
                data.cargoFromFloor = getChecked(R.id.cargo_from_station);
                data.cargoToShip = getChecked(R.id.cargo_to_ship);
                data.cargoToRocket1 = getChecked(R.id.cargo_to_rocket_1);
                data.cargoToRocket2 = getChecked(R.id.cargo_to_rocket_2);
                data.cargoToRocket3 = getChecked(R.id.cargo_to_rocket_3);
                data.cargoFromOpponentSide = getYN(R.id.cargo_from_opponent_side);
                data.hasCamera = getYN(R.id.has_camera);
                data.hatchPreload = getChecked(R.id.hatch_preload);
                data.cargoPreload = getChecked(R.id.cargo_preload);
                data.willingToDefend = getYN(R.id.willing_to_defend);
                data.groundClearance = getFloat(R.id.ground_clearance);
                data.comments = DataUtil.clean(((EditText)findViewById(R.id.comments)).getText().toString());

                pitData.data = data;
                pitData.scouted = true;
                new SavePitDataThread(pitData, new SavePitDataThread.Listener() {
                    @Override
                    public void onTaskComplete() {
                        finish();
                    }
                }).start();
            }
        });
    }

    private float getFloat(int id) {
        try {
            return Float.parseFloat(((EditText) findViewById(id)).getText().toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String getOption(int id) {
        return ((Spinner)findViewById(id)).getSelectedItem().toString();
    }

    private boolean getChecked(int id) {
        return ((CheckBox)findViewById(id)).isChecked();
    }

    private boolean getYN(int id) {
        return ((Spinner)findViewById(id)).getSelectedItemPosition() == 0;
    }
}
