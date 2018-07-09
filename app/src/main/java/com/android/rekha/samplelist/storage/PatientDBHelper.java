package com.android.rekha.samplelist.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.android.rekha.samplelist.R;
import com.android.rekha.samplelist.model.Patient;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * PatientDBHelper class communicates with Patient database and provides all functions around it.<br>
 * <i>Created by Rekha on 7/9/2018.</i>
 */

public class PatientDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "patient.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Patient";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATIENT_NAME = "name";
    public static final String COLUMN_PATIENT_AGE = "age";
    public static final String COLUMN_PATIENT_STATUS = "status";


    /**
     * PatientDBHelper instance can be used to communicate with patient table in database.
     *
     * @param context
     */
    public PatientDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PATIENT_NAME + " TEXT NOT NULL, " +
                COLUMN_PATIENT_AGE + " NUMBER NOT NULL, " +
                COLUMN_PATIENT_STATUS + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    /**
     * Save specified patient details in database under patient table.
     *
     * @param patient
     */
    public long saveNewpatient(Patient patient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATIENT_NAME, patient.getName());
        values.put(COLUMN_PATIENT_AGE, patient.getAge());
        values.put(COLUMN_PATIENT_STATUS, patient.getStatus());

        // insert
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**
     * Removes record of patient with specified id from database.
     *
     * @param id
     * @param context
     */
    public void deletePatientRecord(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "='" + id + "'");
        Toast.makeText(context, context.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates record of patient with specified id in database.
     *
     * @param patientId
     * @param context
     * @param updatedPateint
     */
    public long updatePatientRecord(long patientId, Context context, Patient updatedPateint) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE  " + TABLE_NAME + " SET " + COLUMN_PATIENT_NAME + " ='" + updatedPateint.getName() + "', " +
                COLUMN_PATIENT_AGE + " ='" + updatedPateint.getAge() + "', " + COLUMN_PATIENT_STATUS + " ='" +
                updatedPateint.getStatus() + "'  WHERE " + COLUMN_ID + "='" + patientId + "'");
        Toast.makeText(context, context.getString(R.string.updated), Toast.LENGTH_SHORT).show();
        return patientId;
    }

    /**
     * This method will return detail of patient with specified id.
     *
     * @param id patinet id
     * @return patinet instance filled with detail
     */
    public Patient getPatientFor(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=" + id;
        Cursor cursor = db.rawQuery(query, null);

        Patient receivedPatient = new Patient();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedPatient.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_NAME)));
            receivedPatient.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_AGE)));
            receivedPatient.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_STATUS)));
        }
        return receivedPatient;
    }

    /**
     * Returns all the patients details which are present in database.
     *
     * @return - patient instance list.
     */
    public List<Patient> getAllPatients() {
        String query = "SELECT  * FROM " + TABLE_NAME + " order by " + COLUMN_ID +" DESC ";

        List<Patient> patientLinkedList = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Patient patient;

        if (cursor.moveToFirst()) {
            do {
                patient = new Patient();
                patient.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                patient.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_NAME)));
                patient.setAge(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_AGE)));
                patient.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_PATIENT_STATUS)));
                patientLinkedList.add(patient);
            } while (cursor.moveToNext());
        }
        return patientLinkedList;
    }

    // Note - for sample purpose only
    public void saveDummyPatients() {
        ArrayList<Patient> cannedPatinetList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        cannedPatinetList.add(new Patient("Alice", "32", "Admitted"));
        cannedPatinetList.add(new Patient("Jack", "56", "Transferred"));
        cannedPatinetList.add(new Patient("Anne", "24", "Discharged"));
        cannedPatinetList.add(new Patient("Emy", "54", "Admitted"));
        cannedPatinetList.add(new Patient("Robin", "32", "Admitted"));
        cannedPatinetList.add(new Patient("Rahul", "56", "Transferred"));
        cannedPatinetList.add(new Patient("Christ", "24", "Discharged"));
        cannedPatinetList.add(new Patient("Anne", "54", "Admitted"));
        for (Patient patient : cannedPatinetList) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PATIENT_NAME, patient.getName());
            values.put(COLUMN_PATIENT_AGE, patient.getAge());
            values.put(COLUMN_PATIENT_STATUS, patient.getStatus());
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }
}
