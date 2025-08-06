package com.example.b07demosummer2024.emergency;

import java.util.Map;

public class EmergencyContact extends BaseEmergencyItem {
    private String name;
    private String relationship;
    private String phone;

    public EmergencyContact() {
        super();
    }

    public EmergencyContact(String name, String relationship, String phone) {
        super(name);
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
        this.title = name;
    }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("name", name);
        map.put("relationship", relationship);
        map.put("phone", phone);
        return map;
    }
}

