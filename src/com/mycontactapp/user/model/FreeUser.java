package com.mycontactapp.user.model;

/**
 * FreeUser
 * A basic user type with a limit on how many contacts they can add.
 *
 * @author Developer
 * @version 1.0
 */
public class FreeUser extends User {
    
    private static final int MAX_CONTACTS = 100;

    /**
     * Constructs a new FreeUser.
     *
     * @param email        The user's registered email address
     * @param passwordHash The securely hashed password
     * @param fullName     The user's full display name
     */
    public FreeUser(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName);
    }

    /**
     * Retrieves the contact limit for a free user.
     *
     * @return 100 (The maximum allowed contacts)
     */
    @Override
    public int getContactLimit() {
        return MAX_CONTACTS;
    }
}