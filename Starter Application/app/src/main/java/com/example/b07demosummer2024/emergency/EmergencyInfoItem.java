package com.example.b07demosummer2024.emergency;

import java.util.Map;

public interface EmergencyInfoItem {
    String getId();
    void setId(String id);
    String getTitle();
    long getCreatedAt();
    long getUpdatedAt();
    void setUpdatedAt(long updatedAt);
    Map<String, Object> toMap();
}
