package com.mycontactapp.user.model;

/**
 * PremiumUser
 * Represents a paid user with unlimited contact storage capabilities.
 * @author Developer
 * @version 2.0
 */
public class PremiumUser extends User {

    private static final int MAX_CONTACTS = Integer.MAX_VALUE;

    /**
     * Constructs a new PremiumUser.
     * @param email The user's email
     * @param passwordHash The hashed password
     * @param fullName The user's full name
     */
    public PremiumUser(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName);
    }

    /**
     * Constructs a PremiumUser from stored data.
     * @param email The user's email
     * @param passwordHash The hashed password
     * @param fullName The user's full name
     * @param userId The existing user ID
     */
    public PremiumUser(String email, String passwordHash, String fullName, String userId) {
        super(email, passwordHash, fullName, userId);
    }

    /**
     * Returns the maximum number of contacts allowed for a premium user.
     * @return Integer.MAX_VALUE (effectively unlimited)
     */
    @Override
    public int getContactLimit() {
        return MAX_CONTACTS;
    }
}