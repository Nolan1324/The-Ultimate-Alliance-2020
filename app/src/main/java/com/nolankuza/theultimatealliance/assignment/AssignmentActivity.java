package com.nolankuza.theultimatealliance.assignment;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nolankuza.theultimatealliance.BaseActivity;
import com.nolankuza.theultimatealliance.R;
import com.nolankuza.theultimatealliance.room.AssignmentDao;
import com.nolankuza.theultimatealliance.room.StudentDao;
import com.nolankuza.theultimatealliance.structure.Assignment;
import com.nolankuza.theultimatealliance.structure.DeviceInfo;
import com.nolankuza.theultimatealliance.util.Sync;

import java.util.List;

import static com.nolankuza.theultimatealliance.ApplicationState.database;

public class AssignmentActivity extends BaseActivity {

    private BluetoothAdapter bAdapter;
    private List<Assignment> assignments;
    private List<String> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setToolBarMenu(isMaster ? R.menu.toolbar_sync : R.menu.toolbar_sync_slave);
        setContentView(R.layout.activity_assignment);
        super.onCreate(savedInstanceState);

        //Get info for bluetooth devices
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        final List<DeviceInfo> deviceInfos = Sync.getDeviceInfos(bAdapter);

        new Thread() {
            @Override
            public void run() {
                AssignmentDao assignmentDao = database.assignmentDao();
                StudentDao studentDao = database.studentDao();
                for(DeviceInfo deviceInfo : deviceInfos) {
                    String deviceName = deviceInfo.getName();
                    if(assignmentDao.getByName(deviceName).size() == 0) {
                        Assignment assignment = new Assignment();
                        assignment.name = deviceName;
                        assignmentDao.insert(assignment);
                    }
                }
                assignments = assignmentDao.getAll();
                students = studentDao.getNames();
                students.add(0, "Anonymous");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadAdapter();
                    }
                });
            }
        }.start();
    }

    protected void loadAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView assignmentRecycler = findViewById(R.id.assignment_recycler);
        assignmentRecycler.setLayoutManager(layoutManager);
        assignmentRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        AssignmentAdapter assignmentAdapter = new AssignmentAdapter(this, assignments, students);
        assignmentRecycler.setAdapter(assignmentAdapter);
    }
}
