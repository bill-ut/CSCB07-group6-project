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
import com.example.b07demosummer2024.emergency.Document;

import java.util.ArrayList;
import java.util.List;

public class DocumentFragment extends Fragment {

    private DocumentAdapter adapter;
    private FirebaseEmergencyRepository<Document> repository;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_document, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // Initialize repository
            repository = new FirebaseEmergencyRepository<>("documents", Document.class);

            // Setup RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.documentRecycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new DocumentAdapter(new ArrayList<>(), new DocumentAdapter.OnDocumentActionListener() {
                @Override
                public void onEditDocument(Document document) {
                    showEditDocumentDialog(document);
                }

                @Override
                public void onDeleteDocument(Document document) {
                    deleteDocument(document);
                }
            });
            recyclerView.setAdapter(adapter);

            // Load documents
            loadDocuments();

            // Set up add button
            Button addButton = view.findViewById(R.id.addDocumentButton);
            addButton.setOnClickListener(v -> showAddDocumentDialog());

            Button backButton = view.findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                Navigation.findNavController(v).navigateUp();
            });

        } catch (Exception e) {
            Log.e("DocumentFragment", "Error setting up document view", e);
            Toast.makeText(getContext(), "Error loading documents", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDocuments() {
        repository.getAllItems(new EmergencyInfoRepository.OnListFetchedListener<Document>() {
            @Override
            public void onSuccess(List<Document> documents) {
                adapter.setDocuments(documents);
            }

            @Override
            public void onFailure(String error) {
                Log.e("DocumentFragment", "Error loading documents: " + error);
                Toast.makeText(getContext(), "Error loading documents", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddDocumentDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Document name (e.g., Passport, Birth Certificate)");

        new AlertDialog.Builder(getContext())
                .setTitle("Add Document to Pack")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String documentName = input.getText().toString().trim();
                    if (!documentName.isEmpty()) {
                        Document document = new Document(documentName);
                        addDocument(document);
                    } else {
                        Toast.makeText(getContext(), "Please enter a document name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addDocument(Document document) {
        repository.addItem(document, new EmergencyInfoRepository.OnOperationCompleteListener<Document>() {
            @Override
            public void onSuccess(Document item) {
                Toast.makeText(getContext(), "Document added successfully", Toast.LENGTH_SHORT).show();
                loadDocuments();
            }

            @Override
            public void onFailure(String error) {
                Log.e("DocumentFragment", "Error adding document: " + error);
                Toast.makeText(getContext(), "Error adding document", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDocumentDialog(Document document) {
        EditText input = new EditText(getContext());
        input.setText(document.getDocumentName());

        new AlertDialog.Builder(getContext())
                .setTitle("Edit Document")
                .setView(input)
                .setPositiveButton("Update", (dialog, which) -> {
                    String documentName = input.getText().toString().trim();
                    if (!documentName.isEmpty()) {
                        document.setDocumentName(documentName);
                        updateDocument(document);
                    } else {
                        Toast.makeText(getContext(), "Please enter a document name", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateDocument(Document document) {
        repository.updateItem(document, new EmergencyInfoRepository.OnOperationCompleteListener<Document>() {
            @Override
            public void onSuccess(Document item) {
                Toast.makeText(getContext(), "Document updated successfully", Toast.LENGTH_SHORT).show();
                loadDocuments();
            }

            @Override
            public void onFailure(String error) {
                Log.e("DocumentFragment", "Error updating document: " + error);
                Toast.makeText(getContext(), "Error updating document", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDocument(Document document) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Document")
                .setMessage("Are you sure you want to delete \"" + document.getDocumentName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repository.deleteItem(document.getId(), new EmergencyInfoRepository.OnDeleteCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "Document deleted successfully", Toast.LENGTH_SHORT).show();
                            loadDocuments();
                        }

                        @Override
                        public void onFailure(String error) {
                            Log.e("DocumentFragment", "Error deleting document: " + error);
                            Toast.makeText(getContext(), "Error deleting document", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
