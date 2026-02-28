package com.mycontactapp.user.model;

/**
 * FreeUser
 * Represents a standard user with limited contact storage capabilities.
 *
 * @author Developer
 * @version 2.0
 */
public class FreeUser extends User {
    
    private static final int MAX_CONTACTS = 100;

    public FreeUser(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName);
    }

    // NEW constructor for loading from file
    public FreeUser(String email, String passwordHash, String fullName, String userId) {
        super(email, passwordHash, fullName, userId);
    }

    @Override
    public int getContactLimit() {
        return MAX_CONTACTS;
    }
}