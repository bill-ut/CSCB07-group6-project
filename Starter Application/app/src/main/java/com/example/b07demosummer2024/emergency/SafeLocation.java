package com.example.b07demosummer2024.emergency;

import java.util.Map;

public class SafeLocation extends BaseEmergencyItem {
    private String locationName;
    private String address;
    private String notes;

    public SafeLocation() {
        super();
    }

    public SafeLocation(String locationName, String address, String notes) {
        super(locationName);
        this.locationName = locationName;
        this.address = address;
        this.notes = notes;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
        this.title = locationName;  // Keep title in sync
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("locationName", locationName);
        map.put("address", address);
        map.put("notes", notes);
        return map;
    }
}
