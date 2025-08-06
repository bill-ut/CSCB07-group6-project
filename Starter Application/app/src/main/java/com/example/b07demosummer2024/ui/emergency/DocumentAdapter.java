package com.example.b07demosummer2024.ui.emergency;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.emergency.Document;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    private List<Document> documentList;
    private OnDocumentActionListener listener;

    public interface OnDocumentActionListener {
        void onEditDocument(Document document);
        void onDeleteDocument(Document document);
    }

    public DocumentAdapter(List<Document> documentList, OnDocumentActionListener listener) {
        this.documentList = documentList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView documentName;
        public Button editButton, deleteButton;
        public TextView documentDescription;
        public TextView fileAttachedText;
        public Button viewFileButton;

        public ViewHolder(View view) {
            super(view);
            documentName = view.findViewById(R.id.documentNameText);
            fileAttachedText = view.findViewById(R.id.fileAttachedText);
            viewFileButton = view.findViewById(R.id.viewFileButton);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
            documentDescription = view.findViewById(R.id.documentDescriptionText);
        }
    }

    @NonNull
    @Override
    public DocumentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentAdapter.ViewHolder holder, int position) {
        Document document = documentList.get(position);
        holder.documentName.setText(document.getDocumentName());

        // Handle description
        if (holder.documentDescription != null) {
            String description = document.getDescription();
            if (description != null && !description.isEmpty()) {
                holder.documentDescription.setText(description);
                holder.documentDescription.setVisibility(View.VISIBLE);
            } else {
                holder.documentDescription.setVisibility(View.GONE);
            }
        }

        // Handle file attachment - NEW
        if (document.hasFile()) {
            holder.fileAttachedText.setVisibility(View.VISIBLE);
            holder.viewFileButton.setVisibility(View.VISIBLE);
            holder.viewFileButton.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(document.getFileUrl()));
                v.getContext().startActivity(browserIntent);
            });
        } else {
            holder.fileAttachedText.setVisibility(View.GONE);
            holder.viewFileButton.setVisibility(View.GONE);
        }

        if (listener != null) {
            holder.editButton.setOnClickListener(v -> listener.onEditDocument(document));
            holder.deleteButton.setOnClickListener(v -> listener.onDeleteDocument(document));
        }
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public void setDocuments(List<Document> newList) {
        this.documentList = newList;
        notifyDataSetChanged();
    }
}
