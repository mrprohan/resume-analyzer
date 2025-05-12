package com.resumeanalyzer.resume_analyzer.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Resume {

    private String id;
    private String userId; // Optional: Keep if you want to associate user later
    private String originalFileName;
    private String uploadedAt;
    private String extractedText;
    private Map<String, Object> feedback;

    // Empty constructor (⚡ Required for Firebase Firestore)
    public Resume() {}

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {  // <-- Added Setter for id (important for Firestore saving)
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public LocalDateTime getUploadDateTime(){
        return uploadedAt !=null ? LocalDateTime.parse(uploadedAt):null;
    }

    public void setUploadedDateTime(LocalDateTime dateTime){
        this.uploadedAt=dateTime !=null?dateTime.toString():null;
    }

    public String getExtractedText() {
        return extractedText;
    }

    public void setExtractedText(String extractedText) {
        this.extractedText = extractedText;
    }

    public Map<String ,Object> getFeedback() {
        return feedback;
    }

    public void setFeedback(Map<String, Object> feedback) {
        this.feedback = feedback;
    }
}
