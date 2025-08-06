package com.example.b07demosummer2024.data;

import com.example.b07demosummer2024.emergency.EmergencyInfoItem;
import java.util.List;

public interface EmergencyInfoRepository<T extends EmergencyInfoItem> {

    interface OnOperationCompleteListener<T> {
        void onSuccess(T item);
        void onFailure(String error);
    }

    interface OnListFetchedListener<T> {
        void onSuccess(List<T> items);
        void onFailure(String error);
    }

    interface OnDeleteCompleteListener {
        void onSuccess();
        void onFailure(String error);
    }

    void addItem(T item, OnOperationCompleteListener<T> listener);
    void updateItem(T item, OnOperationCompleteListener<T> listener);
    void deleteItem(String id, OnDeleteCompleteListener listener);
    void getAllItems(OnListFetchedListener<T> listener);
    void getItemById(String id, OnOperationCompleteListener<T> listener);
}
