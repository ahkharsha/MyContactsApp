package com.mycontactapp.user.model;

/**
 * PremiumUser
 * A paid user type with no limit on contacts.
 *
 * @author Developer
 * @version 1.0
 */
public class PremiumUser extends User {

    private static final int MAX_CONTACTS = Integer.MAX_VALUE;

    /**
     * Constructs a new PremiumUser.
     *
     * @param email        The user's registered email address
     * @param passwordHash The securely hashed password
     * @param fullName     The user's full display name
     */
    public PremiumUser(String email, String passwordHash, String fullName) {
        super(email, passwordHash, fullName);
    }

    /**
     * Retrieves the contact limit for a premium user.
     *
     * @return Integer.MAX_VALUE (Unlimited contacts)
     */
    @Override
    public int getContactLimit() {
        return MAX_CONTACTS;
    }
}