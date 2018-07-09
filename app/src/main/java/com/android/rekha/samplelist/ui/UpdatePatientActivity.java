package com.android.rekha.samplelist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rekha.samplelist.R;
import com.android.rekha.samplelist.model.Patient;
import com.android.rekha.samplelist.storage.PatientDBHelper;
import com.android.rekha.samplelist.util.Constants;

/**
 * UpdatePatientActivity allows user to update or save a patient details.
 * <i>Created by rekha on 7/9/2018. >/i>
 */

public class UpdatePatientActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mAgeEditText;
    private EditText mStatusEditText;
    private Button mUpdateBtn;

    private PatientDBHelper mDbHelper;
    private long mReceivedPatientId;
    private String mReceivedPatientAction;
    private TextView mTitleText;
    private int mReceivedPatientIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_patient_layout);
        initView();
        registerViewActions();
    }

    private void registerViewActions() {
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdateAction())
                    updatePatient();
                else
                    savePatient();
            }
        });
    }

    private void initView() {
        mNameEditText = (EditText) findViewById(R.id.patient_name_edittext);
        mAgeEditText = (EditText) findViewById(R.id.patient_age_edittext);
        mStatusEditText = (EditText) findViewById(R.id.patient_status_edittext);
        mUpdateBtn = (Button) findViewById(R.id.action_button);
        mTitleText = findViewById(R.id.title_txtview);

        mDbHelper = new PatientDBHelper(this);

        try {
            //get intent to get patient id
            mReceivedPatientId = getIntent().getLongExtra(Constants.EXTRA_ID, 1);
            mReceivedPatientAction = getIntent().getStringExtra(Constants.EXTRA_ACTION_TYPE);
            mReceivedPatientIndex = getIntent().getIntExtra(Constants.EXTRA_POSITION, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isUpdateAction()) {
            // populate patient data before update
            Patient queriedPatient = mDbHelper.getPatientFor(mReceivedPatientId);
            // set field to this patient data
            mNameEditText.setText(queriedPatient.getName());
            mAgeEditText.setText(queriedPatient.getAge());
            mStatusEditText.setText(queriedPatient.getStatus());
            mUpdateBtn.setText(getString(R.string.update_user));
            mTitleText.setText(getString(R.string.update_user));
        } else {
            mUpdateBtn.setText(getString(R.string.add_user));
            mTitleText.setText(getString(R.string.add_user));
        }
    }

    private boolean isUpdateAction() {
        return ((!(TextUtils.isEmpty(mReceivedPatientAction)) && mReceivedPatientAction.equalsIgnoreCase(Constants.EXTRA_ACTION_UPDATE)));
    }

    private void updatePatient() {
        String name = mNameEditText.getText().toString().trim();
        String age = mAgeEditText.getText().toString().trim();
        String status = mStatusEditText.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || status.isEmpty()) {
            //error something is empty
            Toast.makeText(this, getString(R.string.name_age_status_cannot_empty), Toast.LENGTH_SHORT).show();
        } else {
            //create updated patient
            Patient updatedPatient = new Patient(name, age, status);
            //call dbhelper update
            int id = (int) mDbHelper.updatePatientRecord(mReceivedPatientId, this, updatedPatient);
            //finally redirect back home
            goBackHome(id);
        }
    }

    private void savePatient() {
        String name = mNameEditText.getText().toString().trim();
        String age = mAgeEditText.getText().toString().trim();
        String status = mStatusEditText.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || status.isEmpty()) {
            //error something is empty
            Toast.makeText(this, getString(R.string.name_age_status_cannot_empty), Toast.LENGTH_SHORT).show();
        } else {
            //create new patient
            Patient patient = new Patient(name, age, status);
            long id = mDbHelper.saveNewpatient(patient);
            //finally redirect back home
            goBackHome(id);
        }
    }

    private void goBackHome(long id) {
        setResult(RESULT_OK, new Intent().putExtra(Constants.EXTRA_ID, id).putExtra(Constants.EXTRA_POSITION, mReceivedPatientIndex));
        finish();
    }
}
