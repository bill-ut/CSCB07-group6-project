package com.example.b07demosummer2024.emergency;

import java.util.Map;

    public class Document extends BaseEmergencyItem {
        private String documentName;
        private String fileUrl;
        private String fileName;
        private String description;

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
            this.title = documentName;
        }

        @Override
        public Map<String, Object> toMap() {
            Map<String, Object> map = super.toMap();
            map.put("documentName", documentName);
            map.put("fileUrl", fileUrl);
            map.put("fileName", fileName);
            map.put("description", description);
            return map;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean hasFile() {
            return fileUrl != null && !fileUrl.isEmpty();
        }
    }
