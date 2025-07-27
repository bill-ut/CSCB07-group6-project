package com.example.b07demosummer2024.ui.emergency;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b07demosummer2024.R;
import com.example.b07demosummer2024.emergency.EmergencyContact;

import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ViewHolder> {

    private List<EmergencyContact> contactList;

    public EmergencyContactAdapter(List<EmergencyContact> contactList) {
        this.contactList = contactList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, relationship, phone;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.contactNameText);
            relationship = view.findViewById(R.id.contactRelationshipText);
            phone = view.findViewById(R.id.contactPhoneText);
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
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void updateList(List<EmergencyContact> newList) {
        this.contactList = newList;
        notifyDataSetChanged();
    }
}
