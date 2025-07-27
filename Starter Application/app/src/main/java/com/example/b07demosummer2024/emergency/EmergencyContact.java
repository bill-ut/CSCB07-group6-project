package com.example.b07demosummer2024.emergency;

public class EmergencyContact {
    public String name;
    public String relationship;
    public String phone;

    public EmergencyContact() {
    }

    public EmergencyContact(String name, String relationship, String phone) {
        this.name = name;
        this.relationship = relationship;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

