package com.resumeanalyzer.resume_analyzer.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.resumeanalyzer.resume_analyzer.model.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserFirebaseService {

    private static final String COLLECTION_NAME = "users";

    // Save User
    public String saveUser(User user) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document();
        user.setId(docRef.getId());
        ApiFuture<WriteResult> future = docRef.set(user);
        return docRef.getId();
    }

    // Find User by Email
    public User findUserByEmail(String email) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference users = db.collection(COLLECTION_NAME);

        Query query = users.whereEqualTo("email", email).limit(1);
        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot snapshot = future.get();

        if (!snapshot.isEmpty()) {
            DocumentSnapshot document = snapshot.getDocuments().get(0);
            return document.toObject(User.class);
        } else {
            return null; // ❌ User not found
        }
    }
}
