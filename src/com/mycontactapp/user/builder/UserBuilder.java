package com.mycontactapp.user.builder;

/**
 * UserBuilder
 * Constructs user data step-by-step before delegating object creation to the factory.
 *
 * @author Developer
 * @version 1.0
 */
public class UserBuilder {

    private String email;
    private String passwordHash;
    private String fullName;
    private String userId;

    /**
     * Stores the email for the user being built.
     * @param email The user email
     * @return The same builder instance
     */
    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Stores the hashed password for the user being built.
     * @param passwordHash The hashed password
     * @return The same builder instance
     */
    public UserBuilder setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    /**
     * Stores the full name for the user being built.
     * @param fullName The user full name
     * @return The same builder instance
     */
    public UserBuilder setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    /**
     * Stores an existing user ID when reconstructing persisted users.
     * @param userId The persisted user ID
     * @return The same builder instance
     */
    public UserBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Retrieves the configured email value.
     * @return The configured email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the configured password hash.
     * @return The configured password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Retrieves the configured full name.
     * @return The configured full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Retrieves the configured persisted user ID.
     * @return The persisted user ID, or null for brand new users
     */
    public String getUserId() {
        return userId;
    }
}