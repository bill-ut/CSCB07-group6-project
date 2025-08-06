package com.example.b07demosummer2024.ui.emergency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.emergency.EmergencyContact;

import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder> {

    private List<EmergencyContact> contactList;
    private OnContactActionListener listener;

    // Interface for handling edit and delete actions
    public interface OnContactActionListener {
        void onEditContact(EmergencyContact contact);
        void onDeleteContact(EmergencyContact contact);
    }

    public EmergencyContactAdapter(List<EmergencyContact> contactList) {
        this.contactList = contactList;
    }

    public EmergencyContactAdapter(List<EmergencyContact> contactList, OnContactActionListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    public void setOnContactActionListener(OnContactActionListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, relationship, phone;
        public Button editButton, deleteButton;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.contactNameText);
            relationship = view.findViewById(R.id.contactRelationshipText);
            phone = view.findViewById(R.id.contactPhoneText);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public EmergencyContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyContactAdapter.ViewHolder holder, int position) {
        EmergencyContact contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.relationship.setText(contact.getRelationship());
        holder.phone.setText(contact.getPhone());

        // Set up button click listeners
        if (listener != null) {
            holder.editButton.setOnClickListener(v -> listener.onEditContact(contact));
            holder.deleteButton.setOnClickListener(v -> listener.onDeleteContact(contact));
        }
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void setContacts(List<EmergencyContact> newList) {
        this.contactList = newList;
        notifyDataSetChanged();
    }

    public void addContact(EmergencyContact contact) {
        contactList.add(contact);
        notifyItemInserted(contactList.size() - 1);
    }

    public List<EmergencyContact> getContacts() {
        return contactList;
    }
}
