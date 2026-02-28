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
    
    /**
     * Updates the user's email address.
     * @param email The new email address
     * @throws IllegalArgumentException if the email is null or empty
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) throw new IllegalArgumentException("Email cannot be null or empty.");
        this.email = email;
    }

    /**
     * Gets the hashed password.
     * @return The password hash
     */
    public String getPasswordHash() { return passwordHash; }
    
    /**
     * Updates the user's password hash.
     * @param passwordHash The new password hash
     * @throws IllegalArgumentException if the hash is null or empty
     */
    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) throw new IllegalArgumentException("Password hash cannot be null or empty.");
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the user's full name.
     * @return The full name
     */
    public String getFullName() { return fullName; }
    
    /**
     * Updates the user's full name.
     * @param fullName The new full name
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) throw new IllegalArgumentException("Full name cannot be null or empty.");
        this.fullName = fullName;
    }

    /**
     * Gets the maximum number of contacts this user is allowed to store.
     * This is determined by the specific subclass (Free vs Premium).
     * @return The contact limit
     */
    public abstract int getContactLimit();
}