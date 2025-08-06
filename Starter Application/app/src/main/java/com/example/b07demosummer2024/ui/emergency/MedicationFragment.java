package com.example.b07demosummer2024.ui.emergency;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.data.EmergencyInfoRepository;
import com.example.b07demosummer2024.data.FirebaseEmergencyRepository;
import com.example.b07demosummer2024.emergency.Medication;

import java.util.ArrayList;
import java.util.List;

public class MedicationFragment extends Fragment {

    private MedicationAdapter adapter;
    private FirebaseEmergencyRepository<Medication> repository;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Initialize repository
            repository = new FirebaseEmergencyRepository<>("medications", Medication.class);

            // Setup RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.medicationRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new MedicationAdapter(new ArrayList<>(), new MedicationAdapter.OnMedicationActionListener() {
                @Override
                public void onEditMedication(Medication medication) {
                    showEditMedicationDialog(medication);
                }

                @Override
                public void onDeleteMedication(Medication medication) {
                    deleteMedication(medication);
                }
            });
            recyclerView.setAdapter(adapter);

            // Load medications
            loadMedications();

            // Set up add button
            Button addButton = view.findViewById(R.id.addMedicationButton);
            addButton.setOnClickListener(v -> showAddMedicationDialog());

            Button backButton = view.findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                Navigation.findNavController(v).navigateUp();
            });

        } catch (Exception e) {
            Log.e("MedicationFragment", "Error setting up medication view", e);
            Toast.makeText(getContext(), "Error loading medications", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMedications() {
        repository.getAllItems(new EmergencyInfoRepository.OnListFetchedListener<Medication>() {
            @Override
            public void onSuccess(List<Medication> medications) {
                adapter.setMedications(medications);
            }

            @Override
            public void onFailure(String error) {
                Log.e("MedicationFragment", "Error loading medications: " + error);
                Toast.makeText(getContext(), "Error loading medications", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddMedicationDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_medication, null);
        EditText nameInput = dialogView.findViewById(R.id.medicationNameInput);
        EditText dosageInput = dialogView.findViewById(R.id.medicationDosageInput);

        new AlertDialog.Builder(getContext())
                .setTitle("Add Medication")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String dosage = dosageInput.getText().toString().trim();

                    if (!name.isEmpty() && !dosage.isEmpty()) {
                        Medication medication = new Medication(name, dosage);
                        addMedication(medication);
                    } else {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addMedication(Medication medication) {
        repository.addItem(medication, new EmergencyInfoRepository.OnOperationCompleteListener<Medication>() {
            @Override
            public void onSuccess(Medication item) {
                Toast.makeText(getContext(), "Medication added successfully", Toast.LENGTH_SHORT).show();
                loadMedications();
            }

            @Override
            public void onFailure(String error) {
                Log.e("MedicationFragment", "Error adding medication: " + error);
                Toast.makeText(getContext(), "Error adding medication", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditMedicationDialog(Medication medication) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_medication, null);
        EditText nameInput = dialogView.findViewById(R.id.medicationNameInput);
        EditText dosageInput = dialogView.findViewById(R.id.medicationDosageInput);

        // Pre-fill with existing data
        nameInput.setText(medication.getMedicationName());
        dosageInput.setText(medication.getDosage());

        new AlertDialog.Builder(getContext())
                .setTitle("Edit Medication")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String dosage = dosageInput.getText().toString().trim();

                    if (!name.isEmpty() && !dosage.isEmpty()) {
                        medication.setMedicationName(name);
                        medication.setDosage(dosage);
                        updateMedication(medication);
                    } else {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateMedication(Medication medication) {
        repository.updateItem(medication, new EmergencyInfoRepository.OnOperationCompleteListener<Medication>() {
            @Override
            public void onSuccess(Medication item) {
                Toast.makeText(getContext(), "Medication updated successfully", Toast.LENGTH_SHORT).show();
                loadMedications();
            }

            @Override
            public void onFailure(String error) {
                Log.e("MedicationFragment", "Error updating medication: " + error);
                Toast.makeText(getContext(), "Error updating medication", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMedication(Medication medication) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Medication")
                .setMessage("Are you sure you want to delete \"" + medication.getMedicationName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteItem(medication.getId(), new EmergencyInfoRepository.OnDeleteCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Medication deleted successfully", Toast.LENGTH_SHORT).show();
                            loadMedications();
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.e("MedicationFragment", "Error deleting medication: " + error);
                            Toast.makeText(getContext(), "Error deleting medication", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
