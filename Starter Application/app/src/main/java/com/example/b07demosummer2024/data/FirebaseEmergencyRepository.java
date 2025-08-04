package com.example.b07demosummer2024.data;

import android.util.Log;
import com.example.b07demosummer2024.emergency.EmergencyInfoItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseEmergencyRepository<T extends EmergencyInfoItem>
        implements EmergencyInfoRepository<T> {

    private static final String TAG = "FirebaseEmergencyRepo";
    private final String collectionName;
    private final Class<T> itemClass;

    public FirebaseEmergencyRepository(String collectionName, Class<T> itemClass) {
        this.collectionName = collectionName;
        this.itemClass = itemClass;
    }

    private DatabaseReference getUserCollectionRef() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (uid == null) {
            return null;
        }
        return FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .child(collectionName);
    }

    @Override
    public void addItem(T item, OnOperationCompleteListener<T> listener) {
        DatabaseReference ref = getUserCollectionRef();
        if (ref == null) {
            listener.onFailure("User not authenticated");
            return;
        }

        String key = ref.push().getKey();
        if (key != null) {
            item.setId(key);
            item.setUpdatedAt(System.currentTimeMillis());

            ref.child(key).setValue(item.toMap())
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Item added successfully");
                        listener.onSuccess(item);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to add item", e);
                        listener.onFailure(e.getMessage());
                    });
        } else {
            listener.onFailure("Failed to generate key");
        }
    }

    @Override
    public void updateItem(T item, OnOperationCompleteListener<T> listener) {
        DatabaseReference ref = getUserCollectionRef();
        if (ref == null) {
            listener.onFailure("User not authenticated");
            return;
        }

        item.setUpdatedAt(System.currentTimeMillis());

        ref.child(item.getId()).setValue(item.toMap())
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Item updated successfully");
                    listener.onSuccess(item);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update item", e);
                    listener.onFailure(e.getMessage());
                });
    }

    @Override
    public void deleteItem(String id, OnDeleteCompleteListener listener) {
        DatabaseReference ref = getUserCollectionRef();
        if (ref == null) {
            listener.onFailure("User not authenticated");
            return;
        }

        ref.child(id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Item deleted successfully");
                    listener.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete item", e);
                    listener.onFailure(e.getMessage());
                });
    }

    @Override
    public void getAllItems(OnListFetchedListener<T> listener) {
        DatabaseReference ref = getUserCollectionRef();
        if (ref == null) {
            listener.onFailure("User not authenticated");
            return;
        }

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<T> items = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        T item = child.getValue(itemClass);
                        if (item != null) {
                            item.setId(child.getKey());
                            items.add(item);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing item", e);
                    }
                }
                listener.onSuccess(items);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error", error.toException());
                listener.onFailure(error.getMessage());
            }
        });
    }

    @Override
    public void getItemById(String id, OnOperationCompleteListener<T> listener) {
        DatabaseReference ref = getUserCollectionRef();
        if (ref == null) {
            listener.onFailure("User not authenticated");
            return;
        }

        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        T item = snapshot.getValue(itemClass);
                        if (item != null) {
                            item.setId(snapshot.getKey());
                            listener.onSuccess(item);
                        } else {
                            listener.onFailure("Failed to parse item");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing item", e);
                        listener.onFailure(e.getMessage());
                    }
                } else {
                    listener.onFailure("Item not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Database error", error.toException());
                listener.onFailure(error.getMessage());
            }
        });
    }
}
