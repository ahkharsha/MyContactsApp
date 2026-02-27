package com.mycontactapp.user.model;

import java.util.UUID;

/**
 * User
 * Represents a user in our system. It holds common details like name and email.
 *
 * @author Developer
 * @version 1.0
 */
public abstract class User {
    private final String userId;
    private String email;
    private String passwordHash;
    private String fullName;

    /**
     * Constructs a new User with validated core details.
     *
     * @param email        The user's registered email address
     * @param passwordHash The securely hashed password
     * @param fullName     The user's full display name
     */
    public User(String email, String passwordHash, String fullName) {
        this.userId = UUID.randomUUID().toString();
        setEmail(email);
        setPasswordHash(passwordHash);
        setFullName(fullName);
    }

    /**
     * Retrieves the unique identifier for the user.
     *
     * @return The UUID string
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the user's email address after basic null/empty checks.
     *
     * @param email The new email address
     * @throws IllegalArgumentException if the email is null or empty
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        this.email = email;
    }

    /**
     * Retrieves the user's hashed password.
     *
     * @return The password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Updates the user's hashed password.
     *
     * @param passwordHash The new hashed password
     * @throws IllegalArgumentException if the hash is null or empty
     */
    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.trim().isEmpty()) {
            throw new IllegalArgumentException("Password hash cannot be null or empty.");
        }
        this.passwordHash = passwordHash;
    }

    /**
     * Retrieves the user's full name.
     *
     * @return The full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Updates the user's full name.
     *
     * @param fullName The new full name
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name cannot be null or empty.");
        }
        this.fullName = fullName;
    }

    /**
     * Abstract method to determine the maximum number of contacts a user can have.
     * Demonstrates polymorphism when implemented by subclasses.
     *
     * @return The contact limit integer
     */
    public abstract int getContactLimit();
}