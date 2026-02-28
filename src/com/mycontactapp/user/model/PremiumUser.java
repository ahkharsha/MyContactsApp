package com.mycontactapp.user.model;

/**
 * PremiumUser
 * Represents a paid user with unlimited contact storage capabilities.
 *
 * @author Developer
 * @version 2.0
 */
public class PremiumUser extends User {

    private static final int MAX_CONTACTS = Integer.MAX_VALUE;

    public PremiumUser(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName);
    }

    // NEW constructor for loading from file
    public PremiumUser(String email, String passwordHash, String fullName, String userId) {
        super(email, passwordHash, fullName, userId);
    }

    @Override
    public int getContactLimit() {
        return MAX_CONTACTS;
    }
}