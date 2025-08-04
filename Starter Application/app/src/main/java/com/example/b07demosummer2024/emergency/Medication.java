package com.example.b07demosummer2024.emergency;

import java.util.Map;

public class Medication extends BaseEmergencyItem {
        private String medicationName;
        private String dosage;

        public Medication() {
            super();
        }

        public Medication(String medicationName, String dosage) {
            super(medicationName);
            this.medicationName = medicationName;
            this.dosage = dosage;
        }

        public String getMedicationName() {
            return medicationName;
        }

        public void setMedicationName(String medicationName) {
            this.medicationName = medicationName;
            this.title = medicationName;  // Keep title in sync
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = super.toMap();
            map.put("medicationName", medicationName);
            map.put("dosage", dosage);
            return map;
        }
    }
