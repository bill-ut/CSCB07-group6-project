package com.example.b07demosummer2024.emergency;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Locale;

public abstract class BaseEmergencyItem implements EmergencyInfoItem {
    protected String id;
    protected String title;
    protected String createdAt;
    protected String updatedAt;

    public BaseEmergencyItem() {
        this.id = UUID.randomUUID().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        this.createdAt = currentDateTime;
        this.updatedAt = currentDateTime;
    }

    public BaseEmergencyItem(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        this.createdAt = currentDateTime;
        this.updatedAt = currentDateTime;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    @Override
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("title", title);
        map.put("createdAt", createdAt);
        map.put("updatedAt", updatedAt);
        return map;
    }
}
