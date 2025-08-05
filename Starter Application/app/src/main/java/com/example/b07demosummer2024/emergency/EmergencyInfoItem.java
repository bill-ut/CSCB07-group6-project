package com.example.b07demosummer2024.emergency;

import java.util.Map;

public interface EmergencyInfoItem {
    String getId();
    void setId(String id);
    String getTitle();
    String getCreatedAt();
    String getUpdatedAt();
    void setUpdatedAt(String updatedAt);
    Map<String, Object> toMap();
}
