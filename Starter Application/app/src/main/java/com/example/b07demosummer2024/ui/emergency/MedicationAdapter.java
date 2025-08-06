package com.example.b07demosummer2024.ui.emergency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.emergency.Medication;

import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    private List<Medication> medicationList;
    private OnMedicationActionListener listener;

    public interface OnMedicationActionListener {
        void onEditMedication(Medication medication);
        void onDeleteMedication(Medication medication);
    }

    public MedicationAdapter(List<Medication> medicationList, OnMedicationActionListener listener) {
        this.medicationList = medicationList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView medicationName, dosage;
        public Button editButton, deleteButton;

        public ViewHolder(View view) {
            super(view);
            medicationName = view.findViewById(R.id.medicationNameText);
            dosage = view.findViewById(R.id.medicationDosageText);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationAdapter.ViewHolder holder, int position) {
        Medication medication = medicationList.get(position);
        holder.medicationName.setText(medication.getMedicationName());
        holder.dosage.setText("Dosage: " + medication.getDosage());

        if (listener != null) {
            holder.editButton.setOnClickListener(v -> listener.onEditMedication(medication));
            holder.deleteButton.setOnClickListener(v -> listener.onDeleteMedication(medication));
        }
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public void setMedications(List<Medication> newList) {
        this.medicationList = newList;
        notifyDataSetChanged();
    }
}
