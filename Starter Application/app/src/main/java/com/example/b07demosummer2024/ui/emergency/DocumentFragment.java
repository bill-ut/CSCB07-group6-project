package com.example.b07demosummer2024.ui.emergency;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class DocumentFragment extends Fragment {

    private DocumentAdapter adapter;
    private FirebaseEmergencyRepository<Document> repository;
    private static final int PICK_FILE_REQUEST = 1;
    private Uri selectedFileUri = null;
    private String selectedFileName = null;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private TextView currentSelectedFileText;

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

            // Initialize Firebase Storage
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();

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
        selectedFileUri = null;
        selectedFileName = null;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_document, null);
        EditText nameInput = dialogView.findViewById(R.id.documentNameInput);
        EditText descriptionInput = dialogView.findViewById(R.id.documentDescriptionInput);
        TextView selectedFileText = dialogView.findViewById(R.id.selectedFileText);
        Button attachFileButton = dialogView.findViewById(R.id.attachFileButton);

        attachFileButton.setEnabled(true);
        attachFileButton.setText("Attach File (PDF/Image)");

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add Document to Pack")
                .setView(dialogView)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        attachFileButton.setOnClickListener(v -> {
            currentSelectedFileText = selectedFileText;
            openFilePicker();
        });

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String documentName = nameInput.getText().toString().trim();
                if (!documentName.isEmpty()) {
                    Document document = new Document(documentName);
                    document.setDescription(descriptionInput.getText().toString().trim());

                    if (selectedFileUri != null) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Uploading file...", Toast.LENGTH_SHORT).show();

                        uploadFileToStorage(selectedFileUri, selectedFileName, new OnFileUploadListener() {
                            @Override
                            public void onSuccess(String downloadUrl) {
                                document.setFileUrl(downloadUrl);
                                document.setFileName(selectedFileName);
                                addDocument(document);
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(getContext(), "Upload failed: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        addDocument(document);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Please enter a document name", Toast.LENGTH_SHORT).show();
                }
            });;
        });

        dialog.show();
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
        selectedFileUri = null;
        selectedFileName = null;

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_document, null);
        EditText nameInput = dialogView.findViewById(R.id.documentNameInput);
        EditText descriptionInput = dialogView.findViewById(R.id.documentDescriptionInput);
        TextView selectedFileText = dialogView.findViewById(R.id.selectedFileText);
        Button attachFileButton = dialogView.findViewById(R.id.attachFileButton);

        nameInput.setText(document.getDocumentName());
        descriptionInput.setText(document.getDescription() != null ? document.getDescription() : "");

        if (document.hasFile()) {
            selectedFileText.setText("Current file: " + document.getFileName());
            attachFileButton.setText("Replace File");
        } else {
            attachFileButton.setText("Attach File (PDF/Image)");
        }

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Edit Document")
                .setView(dialogView)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        attachFileButton.setOnClickListener(v -> {
            currentSelectedFileText = selectedFileText;
            openFilePicker();
        });

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                String documentName = nameInput.getText().toString().trim();
                if (!documentName.isEmpty()) {
                    document.setDocumentName(documentName);
                    document.setDescription(descriptionInput.getText().toString().trim());

                    if (selectedFileUri != null) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Uploading new file...", Toast.LENGTH_SHORT).show();

                        uploadFileToStorage(selectedFileUri, selectedFileName, new OnFileUploadListener() {
                            @Override
                            public void onSuccess(String downloadUrl) {
                                document.setFileUrl(downloadUrl);
                                document.setFileName(selectedFileName);
                                updateDocument(document);
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(getContext(), "Upload failed: " + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        updateDocument(document);
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(getContext(), "Please enter a document name", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
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
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"application/pdf", "image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_FILE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                String filename = "Selected file";
                if (selectedFileUri.getLastPathSegment() != null) {
                    filename = selectedFileUri.getLastPathSegment();
                }
                selectedFileName = filename;

                if (currentSelectedFileText != null) {
                    currentSelectedFileText.setText("Selected: " + filename);
                }

                Toast.makeText(getContext(), "File selected: " + filename, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadFileToStorage(Uri fileUri, String fileName, OnFileUploadListener listener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("DocumentFragment", "User ID: " + userId);
        String path = "documents/" + userId + "/" + System.currentTimeMillis() + "_" + fileName;
        Log.d("DocumentFragment", "Uploading file to path: " + path);

        StorageReference fileRef = storageRef.child(path);

        fileRef.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        listener.onSuccess(uri.toString());
                    }).addOnFailureListener(e -> {
                        listener.onFailure("Failed to get download URL: " + e.getMessage());
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("DocumentFragment", "Upload error: " + e.getMessage());
                    listener.onFailure("Upload failed: " + e.getMessage());
                });
    }

    // Interface for upload callback
    private interface OnFileUploadListener {
        void onSuccess(String downloadUrl);
        void onFailure(String error);
    }
}
