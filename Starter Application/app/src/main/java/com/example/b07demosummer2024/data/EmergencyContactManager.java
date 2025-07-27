package com.example.b07demosummer2024.data;

import android.util.Log;

import com.example.b07demosummer2024.emergency.EmergencyContact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EmergencyContactManager {
    private static final String TAG = "EmergencyContactManager";

    public static void saveContact(EmergencyContact contact) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (uid == null) {
            Log.e(TAG, "User not logged in");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child("emergency_contacts");

        ref.push().setValue(contact)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Emergency contact saved"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to save contact", e));
    }
    public void addContact(EmergencyContact contact) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("emergency_contacts");

        String key = ref.push().getKey();
        if (key != null) {
            ref.child(key).setValue(contact);
        }
    }

    public interface OnContactsFetchedListener {
        void onContactsFetched(List<EmergencyContact> contacts);
    }

    public void getAllContacts(OnContactsFetchedListener listener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("emergency_contacts");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<EmergencyContact> contacts = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    EmergencyContact contact = child.getValue(EmergencyContact.class);
                    if (contact != null) contacts.add(contact);
                }
                listener.onContactsFetched(contacts);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                listener.onContactsFetched(new ArrayList<>()); // Return empty if error
            }
        });
    }
}
