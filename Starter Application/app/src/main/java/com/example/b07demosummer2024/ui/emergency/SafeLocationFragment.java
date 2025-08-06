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
import com.example.b07demosummer2024.emergency.SafeLocation;

import java.util.ArrayList;
import java.util.List;

public class SafeLocationFragment extends Fragment {

    private SafeLocationAdapter adapter;
    private FirebaseEmergencyRepository<SafeLocation> repository;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_safe_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Initialize repository
            repository = new FirebaseEmergencyRepository<>("safe_locations", SafeLocation.class);

            // Setup RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.safeLocationRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new SafeLocationAdapter(new ArrayList<>(), new SafeLocationAdapter.OnLocationActionListener() {
                @Override
                public void onEditLocation(SafeLocation location) {
                    showEditLocationDialog(location);
                }

                @Override
                public void onDeleteLocation(SafeLocation location) {
                    deleteLocation(location);
                }
            });
            recyclerView.setAdapter(adapter);

            // Load locations
            loadLocations();

            // Set up add button
            Button addButton = view.findViewById(R.id.addLocationButton);
            addButton.setOnClickListener(v -> showAddLocationDialog());

            Button backButton = view.findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                Navigation.findNavController(v).navigateUp();
            });

        } catch (Exception e) {
            Log.e("SafeLocationFragment", "Error setting up safe location view", e);
            Toast.makeText(getContext(), "Error loading locations", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLocations() {
        repository.getAllItems(new EmergencyInfoRepository.OnListFetchedListener<SafeLocation>() {
            @Override
            public void onSuccess(List<SafeLocation> locations) {
                adapter.setLocations(locations);
            }

            @Override
            public void onFailure(String error) {
                Log.e("SafeLocationFragment", "Error loading locations: " + error);
                Toast.makeText(getContext(), "Error loading locations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddLocationDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_location, null);
        EditText nameInput = dialogView.findViewById(R.id.locationNameInput);
        EditText addressInput = dialogView.findViewById(R.id.locationAddressInput);
        EditText notesInput = dialogView.findViewById(R.id.locationNotesInput);

        new AlertDialog.Builder(getContext())
                .setTitle("Add Safe Location")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String address = addressInput.getText().toString().trim();
                    String notes = notesInput.getText().toString().trim();

                    if (!name.isEmpty() && !address.isEmpty()) {
                        SafeLocation location = new SafeLocation(name, address, notes);
                        addLocation(location);
                    } else {
                        Toast.makeText(getContext(), "Please fill name and address", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addLocation(SafeLocation location) {
        repository.addItem(location, new EmergencyInfoRepository.OnOperationCompleteListener<SafeLocation>() {
            @Override
            public void onSuccess(SafeLocation item) {
                Toast.makeText(getContext(), "Location added successfully", Toast.LENGTH_SHORT).show();
                loadLocations();
            }

            @Override
            public void onFailure(String error) {
                Log.e("SafeLocationFragment", "Error adding location: " + error);
                Toast.makeText(getContext(), "Error adding location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditLocationDialog(SafeLocation location) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_location, null);
        EditText nameInput = dialogView.findViewById(R.id.locationNameInput);
        EditText addressInput = dialogView.findViewById(R.id.locationAddressInput);
        EditText notesInput = dialogView.findViewById(R.id.locationNotesInput);

        // Pre-fill with existing data
        nameInput.setText(location.getLocationName());
        addressInput.setText(location.getAddress());
        notesInput.setText(location.getNotes());

        new AlertDialog.Builder(getContext())
                .setTitle("Edit Safe Location")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String address = addressInput.getText().toString().trim();
                    String notes = notesInput.getText().toString().trim();

                    if (!name.isEmpty() && !address.isEmpty()) {
                        location.setLocationName(name);
                        location.setAddress(address);
                        location.setNotes(notes);
                        updateLocation(location);
                    } else {
                        Toast.makeText(getContext(), "Please fill name and address", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateLocation(SafeLocation location) {
        repository.updateItem(location, new EmergencyInfoRepository.OnOperationCompleteListener<SafeLocation>() {
            @Override
            public void onSuccess(SafeLocation item) {
                Toast.makeText(getContext(), "Location updated successfully", Toast.LENGTH_SHORT).show();
                loadLocations();
            }

            @Override
            public void onFailure(String error) {
                Log.e("SafeLocationFragment", "Error updating location: " + error);
                Toast.makeText(getContext(), "Error updating location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteLocation(SafeLocation location) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Location")
                .setMessage("Are you sure you want to delete \"" + location.getLocationName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteItem(location.getId(), new EmergencyInfoRepository.OnDeleteCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Location deleted successfully", Toast.LENGTH_SHORT).show();
                            loadLocations();
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.e("SafeLocationFragment", "Error deleting location: " + error);
                            Toast.makeText(getContext(), "Error deleting location", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}