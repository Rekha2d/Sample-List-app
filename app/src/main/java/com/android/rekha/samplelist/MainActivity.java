package com.android.rekha.samplelist;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.rekha.samplelist.model.Patient;
import com.android.rekha.samplelist.storage.PatientDBHelper;
import com.android.rekha.samplelist.ui.PatientAdapter;
import com.android.rekha.samplelist.ui.UpdatePatientActivity;
import com.android.rekha.samplelist.util.Constants;

import static com.android.rekha.samplelist.util.Constants.*;

/**
 * MainActivity is the main entrance to teh application.<br>
 * * <i>Created by Rekha on 7/9/2018.</i>
 */
public class MainActivity extends AppCompatActivity implements OnChangeListener {
    private RecyclerView mPatientRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PatientDBHelper mDbHelper;
    private PatientAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        registerEventListeners();
        populatePatientList();
    }

    private void registerEventListeners() {
        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_action);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUpdatePatientActivity();
            }
        });
    }

    private void initViews() {
        //initialize
        mPatientRecyclerView = (RecyclerView) findViewById(R.id.data_list_view);
        mPatientRecyclerView.setHasFixedSize(true);
        // using linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mPatientRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void goToUpdatePatientActivity() {
        Intent intent = new Intent(MainActivity.this, UpdatePatientActivity.class);
        intent.putExtra(EXTRA_ACTION_TYPE, EXTRA_ACTION_ADD);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    private void populatePatientList() {
        mDbHelper = new PatientDBHelper(this);
        if (!PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(Constants.PREF_INIT_LOAD, false)) {
            mDbHelper.saveDummyPatients();
            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(Constants.PREF_INIT_LOAD, true).commit();
        }
        mAdapter = new PatientAdapter(mDbHelper.getAllPatients(), this, this);
        mPatientRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int patientIndex = data.getIntExtra(Constants.EXTRA_POSITION, -1);
            long patientId = data.getLongExtra(Constants.EXTRA_ID, 1);
            if (mDbHelper == null) {
                mDbHelper = new PatientDBHelper(MainActivity.this);
            }
            Patient patient = mDbHelper.getPatientFor(patientId);
            if (requestCode == REQUEST_CODE_UPDATE) {
                mAdapter.udpatePatient(patient, patientIndex);

            } else if (requestCode == REQUEST_CODE_ADD) {
                mAdapter.udpatePatient(patient, -1);
            }
        }
    }

    @Override
    public void onDelete(int index) {
        mPatientRecyclerView.removeViewAt(index);
    }

    @Override
    public void onUpdate(Bundle value) {
        if (value == null) {
            return;
        }
        Intent intent = new Intent(MainActivity.this, UpdatePatientActivity.class);
        intent.putExtra(EXTRA_ACTION_TYPE, EXTRA_ACTION_UPDATE);
        intent.putExtra(EXTRA_POSITION, value.getInt(EXTRA_POSITION));
        intent.putExtra(EXTRA_ID, value.getLong(EXTRA_ID));
        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }
}