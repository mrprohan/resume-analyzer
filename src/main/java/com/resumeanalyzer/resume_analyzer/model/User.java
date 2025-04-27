package com.resumeanalyzer.resume_analyzer.model;

public class User {

    private String id;
    private String name;
    private String email;
    private String password;

    // Empty constructor (⚡ Required for Firebase Firestore)
    public User() {}

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {  // <-- Important to set Firestore document ID
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
