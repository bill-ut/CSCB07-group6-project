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
import com.example.b07demosummer2024.emergency.EmergencyContact;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactFragment extends Fragment {

    private EmergencyContactAdapter adapter;
    private FirebaseEmergencyRepository<EmergencyContact> repository;

    @Nullable
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Just inflate the layout here
        return inflater.inflate(R.layout.fragment_emergency_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Initialize repository
            repository = new FirebaseEmergencyRepository<>("emergency_contacts", EmergencyContact.class);

            // Setup RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.emergencyContactRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new EmergencyContactAdapter(new ArrayList<>(), new EmergencyContactAdapter.OnContactActionListener() {
                @Override
                public void onEditContact(EmergencyContact contact) {
                    showEditContactDialog(contact);
                }

                @Override
                public void onDeleteContact(EmergencyContact contact) {
                    deleteContact(contact);
                }
            });
            recyclerView.setAdapter(adapter);

            // Load contacts
            loadContacts();

            // Set up FAB to show dialog
            Button fab = view.findViewById(R.id.addContactButton);
            fab.setOnClickListener(v -> showAddContactDialog());

            Button backButton = view.findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                Navigation.findNavController(v).navigateUp();
            });

        } catch (Exception e) {
            Log.e("EmergencyContactFrag", "Error setting up emergency contact view", e);
            Toast.makeText(getContext(), "Error loading contacts", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadContacts() {
        repository.getAllItems(new EmergencyInfoRepository.OnListFetchedListener<EmergencyContact>() {
            @Override
            public void onSuccess(List<EmergencyContact> contacts) {
                adapter.setContacts(contacts);
            }

            @Override
            public void onFailure(String error) {
                Log.e("EmergencyContactFrag", "Error loading contacts: " + error);
                Toast.makeText(getContext(), "Error loading contacts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddContactDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_contact, null);
        EditText nameInput = dialogView.findViewById(R.id.contactNameInput);
        EditText relationshipInput = dialogView.findViewById(R.id.contactRelationshipInput);
        EditText phoneInput = dialogView.findViewById(R.id.contactPhoneInput);

        new AlertDialog.Builder(getContext())
                .setTitle("Add Contact")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String relationship = relationshipInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();

                    if (!name.isEmpty() && !relationship.isEmpty() && !phone.isEmpty()) {
                        EmergencyContact contact = new EmergencyContact(name, relationship, phone);
                        addContact(contact);
                    } else {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addContact(EmergencyContact contact) {
        repository.addItem(contact, new EmergencyInfoRepository.OnOperationCompleteListener<EmergencyContact>() {
            @Override
            public void onSuccess(EmergencyContact item) {
                Toast.makeText(getContext(), "Contact added successfully", Toast.LENGTH_SHORT).show();
                loadContacts(); // Refresh the list
            }

            @Override
            public void onFailure(String error) {
                Log.e("EmergencyContactFrag", "Error adding contact: " + error);
                Toast.makeText(getContext(), "Error adding contact", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateContact(EmergencyContact contact) {
        repository.updateItem(contact, new EmergencyInfoRepository.OnOperationCompleteListener<EmergencyContact>() {
            @Override
            public void onSuccess(EmergencyContact item) {
                Toast.makeText(getContext(), "Contact updated successfully", Toast.LENGTH_SHORT).show();
                loadContacts(); // Refresh the list
            }

            @Override
            public void onFailure(String error) {
                Log.e("EmergencyContactFrag", "Error updating contact: " + error);
                Toast.makeText(getContext(), "Error updating contact", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteContact(EmergencyContact contact) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete " + contact.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteItem(contact.getId(), new EmergencyInfoRepository.OnDeleteCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Contact deleted successfully", Toast.LENGTH_SHORT).show();
                            loadContacts(); // Refresh the list
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.e("EmergencyContactFrag", "Error deleting contact: " + error);
                            Toast.makeText(getContext(), "Error deleting contact", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditContactDialog(EmergencyContact contact) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_contact, null);
        EditText nameInput = dialogView.findViewById(R.id.contactNameInput);
        EditText relationshipInput = dialogView.findViewById(R.id.contactRelationshipInput);
        EditText phoneInput = dialogView.findViewById(R.id.contactPhoneInput);

        // Pre-fill with existing data
        nameInput.setText(contact.getName());
        relationshipInput.setText(contact.getRelationship());
        phoneInput.setText(contact.getPhone());

        new AlertDialog.Builder(getContext())
                .setTitle("Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String relationship = relationshipInput.getText().toString().trim();
                    String phone = phoneInput.getText().toString().trim();

                    if (!name.isEmpty() && !relationship.isEmpty() && !phone.isEmpty()) {
                        contact.setName(name);
                        contact.setRelationship(relationship);
                        contact.setPhone(phone);
                        updateContact(contact);
                    } else {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
