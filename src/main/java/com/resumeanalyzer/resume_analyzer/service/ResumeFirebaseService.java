package com.resumeanalyzer.resume_analyzer.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.resumeanalyzer.resume_analyzer.model.Resume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import java.util.concurrent.ExecutionException;

@Service
public class ResumeFirebaseService {

    private static final String COLLECTION_NAME = "resumes";

    @Autowired
    private GeminiService geminiService;

    // Save Resume with AI feedback
    public String saveResume(Resume resume) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();
        resume.setId(docRef.getId());

        // Use plain-text feedback from Gemini (Free Tier)
        String plainFeedback = geminiService.generateFeedback(resume.getExtractedText());
        resume.setFeedback(Map.of("text", plainFeedback));  // Wrap feedback in a simple Map

        ApiFuture<WriteResult> future = docRef.set(resume);
        return docRef.getId();
    }

    // Get Resume by ID
    public Resume getResumeById(String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return document.toObject(Resume.class);
        } else {
            throw new RuntimeException("❌ Resume not found with ID: " + id);
        }
    }
}
