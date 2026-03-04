package com.mycontactapp.auth;

import com.mycontactapp.user.model.User;
import java.util.Optional;

/**
 * Authentication
 * A simple interface that forces every login method (like Password or Google) 
 * to follow the same rules.
 *
 * @author Developer
 * @version 1.0
 */
public interface Authentication {

    /**
     * Attempts to authenticate a user based on provided credentials.
     * @param principal The primary identity of the user (e.g., email)
     * @param credential The proof of identity (e.g., password or OAuth provider name)
     * @return An Optional containing the User if successful, or empty if authentication fails
     */
    Optional<User> authenticate(String principal, String credential);
}