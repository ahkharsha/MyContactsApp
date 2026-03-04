package com.mycontactapp.user.model;

/**
 * FreeUser
 * Represents a standard user with limited contact storage capabilities.
 * @author Developer
 * @version 2.0
 */
public class FreeUser extends User {
    
    private static final int MAX_CONTACTS = 100;

    /**
     * Constructs a new FreeUser.
     * @param email The user's email
     * @param passwordHash The hashed password
     * @param fullName The user's full name
     */
    public FreeUser(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName);
    }

    /**
     * Constructs a FreeUser from stored data.
     * @param email The user's email
     * @param passwordHash The hashed password
     * @param fullName The user's full name
     * @param userId The existing user ID
     */
    public FreeUser(String email, String passwordHash, String fullName, String userId) {
        super(email, passwordHash, fullName, userId);
    }

    /**
     * Returns the maximum number of contacts allowed for a free user.
     * @return 100
     */
    @Override
    public int getContactLimit() {
        return MAX_CONTACTS;
    }
}