package com.example.b07demosummer2024.emergency;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseEmergencyItem implements EmergencyInfoItem {
    protected String id;
    protected String title;
    protected long createdAt;
    protected long updatedAt;

    public BaseEmergencyItem() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public BaseEmergencyItem(String title) {
        this();
        this.title = title;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    @Override
    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    @Override
    public long getCreatedAt() { return createdAt; }

    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    @Override
    public long getUpdatedAt() { return updatedAt; }

    @Override
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }

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
