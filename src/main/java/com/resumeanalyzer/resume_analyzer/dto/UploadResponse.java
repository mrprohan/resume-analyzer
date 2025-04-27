package com.resumeanalyzer.resume_analyzer.dto;

public class UploadResponse {
    private String resumeId;
    private String message;
    private String viewLink;

    public UploadResponse(String resumeId, String message, String viewLink) {
        this.resumeId = resumeId;
        this.message = message;
        this.viewLink = viewLink;
    }

    public String getResumeId() {
        return resumeId;
    }

    public void setResumeId(String resumeId) {
        this.resumeId = resumeId;
    }

    public String getMessage() {
        return message;
    }

    public String getViewLink() {
        return viewLink;
    }
}
