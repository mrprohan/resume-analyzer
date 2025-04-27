package com.resumeanalyzer.resume_analyzer.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.resumeanalyzer.resume_analyzer.model.Resume;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ResumeFirebaseService {

    private static final String COLLECTION_NAME = "resumes";

    // Save Resume
    public String saveResume(Resume resume) throws Exception {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();
        resume.setId(docRef.getId()); // auto-generate document ID
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
