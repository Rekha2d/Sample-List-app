package com.android.rekha.samplelist.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.rekha.samplelist.OnChangeListener;
import com.android.rekha.samplelist.R;
import com.android.rekha.samplelist.model.Patient;
import com.android.rekha.samplelist.storage.PatientDBHelper;
import com.android.rekha.samplelist.util.Constants;

import java.util.List;

/**
 * PatientAdapter plays populates patients data in UI as list.
 * Created by Rekha on 7/9/2018.
 */
public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {
    private List<Patient> mPatientList;
    private Context mContext;
    private OnChangeListener mOnChangeListener;

    /**
     * Patient adapter constructer.
     *
     * @param patientList      - list of patients
     * @param context          - context used
     * @param onChangeListener - callback to indicate the actions perfomed on patient row
     */
    public PatientAdapter(List<Patient> patientList, Context context, OnChangeListener onChangeListener) {
        mPatientList = patientList;
        mContext = context;
        mOnChangeListener = onChangeListener;
    }

    /**
     * Updates the list by modifying single patient.
     *
     * @param patient
     * @param patientIndex
     */
    public void udpatePatient(Patient patient, int patientIndex) {
        if (patientIndex < 0) {
            // there is no index, as its new patinet so add it in end of list.
            mPatientList.add(0, patient);
            notifyItemInserted(0);
            notifyDataSetChanged();
        } else {
            // update the existing patient and reflect the same in the list.
            Patient updatingPateint = mPatientList.get(patientIndex);
            mPatientList.remove(patientIndex);
            mPatientList.add(patientIndex, patient);
            notifyItemChanged(patientIndex);
            notifyDataSetChanged();
        }
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView patientNameTxt;
        public TextView patientAgeTxt;
        public TextView patientStatusTxt;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            patientNameTxt = (TextView) v.findViewById(R.id.name);
            patientAgeTxt = (TextView) v.findViewById(R.id.age);
            patientStatusTxt = (TextView) v.findViewById(R.id.status);
        }
    }

    // Create new row
    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.single_patient_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Patient patient = mPatientList.get(position);
        holder.patientNameTxt.setText(holder.patientAgeTxt.getContext().getText(R.string.name) + " : " + patient.getName());
        holder.patientAgeTxt.setText(holder.patientAgeTxt.getContext().getText(R.string.age) + " : " + patient.getAge());
        holder.patientStatusTxt.setText(holder.patientStatusTxt.getContext().getText(R.string.status) + " : " + patient.getStatus());

        //open option dialog on patient row single click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(v.getContext().getString(R.string.choose_option));
                builder.setMessage(v.getContext().getString(R.string.update_delete, patient.getName()));
                builder.setPositiveButton(v.getContext().getString(R.string.update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //go to update activity
                        goToUpdateActivity(patient.getId(), position);
                    }
                });
                builder.setNeutralButton(v.getContext().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete patient from list and database
                        deletePatient(patient.getId(), position);
                    }
                });
                builder.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void deletePatient(long id, int position) {
        PatientDBHelper dbHelper = new PatientDBHelper(mContext);
        dbHelper.deletePatientRecord(id, mContext);
        mPatientList.remove(position);
        if (mOnChangeListener != null) {
            mOnChangeListener.onDelete(position);
        }

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mPatientList.size());
        notifyDataSetChanged();
    }

    private void goToUpdateActivity(long patientId, int position) {
        if (mOnChangeListener != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(Constants.EXTRA_ID, patientId);
            bundle.putString(Constants.EXTRA_ACTION_TYPE, Constants.EXTRA_ACTION_UPDATE);
            bundle.putInt(Constants.EXTRA_POSITION, position);
            mOnChangeListener.onUpdate(bundle);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPatientList.size();
    }
}