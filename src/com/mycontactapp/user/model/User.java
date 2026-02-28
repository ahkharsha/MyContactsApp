package com.mycontactapp.user.model;

import java.util.UUID;

/**
 * User
 * Abstract base class representing a registered user in the system.
 *
 * @author Developer
 * @version 2.0
 */
public abstract class User {
    private final String userId;
    private String email;
    private String passwordHash;
    private String fullName;

    /**
     * Constructs a brand new User (generates a new UUID).
     */
    public User(String email, String passwordHash, String fullName) {
        this.userId = UUID.randomUUID().toString();
        setEmail(email);
        setPasswordHash(passwordHash);
        setFullName(fullName);
    }

    /**
     * Overloaded constructor for loading an existing User from a file.
     */
    public User(String email, String passwordHash, String fullName, String userId) {
        this.userId = userId;
        setEmail(email);
        setPasswordHash(passwordHash);
        setFullName(fullName);
    }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }
    
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty.");
        this.email = email;
    }

    public String getPasswordHash() { return passwordHash; }
    
    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) throw new IllegalArgumentException("Password hash cannot be null or empty.");
        this.passwordHash = passwordHash;
    }

    public String getFullName() { return fullName; }
    
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) throw new IllegalArgumentException("Full name cannot be null or empty.");
        this.fullName = fullName;
    }

    public abstract int getContactLimit();
}