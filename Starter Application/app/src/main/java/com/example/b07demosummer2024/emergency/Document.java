package com.example.b07demosummer2024.emergency;

import java.util.Map;

    public class Document extends BaseEmergencyItem {
        private String documentName;

        public Document() {
            super();
        }

        public Document(String documentName) {
            super(documentName);
            this.documentName = documentName;
        }

        public String getDocumentName() {
            return documentName;
        }

        public void setDocumentName(String documentName) {
            this.documentName = documentName;
            this.title = documentName;  // Keep title in sync
        }

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = super.toMap();
            map.put("documentName", documentName);
            return map;
        }
    }
