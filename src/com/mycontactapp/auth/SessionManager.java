package com.mycontactapp.auth;

import com.mycontactapp.user.model.User;

/**
 * SessionManager
 * Centralizes authenticated session state using the Singleton pattern.
 *
 * @author Developer
 * @version 1.0
 */
public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();
    private User currentUser;

    /**
     * Prevents external instantiation so only one session manager can exist.
     */
    private SessionManager() {
    }

    /**
     * Retrieves the single shared SessionManager instance.
     * @return The singleton SessionManager
     */
    public static SessionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Stores the authenticated user in the active session.
     * @param user The authenticated user
     */
    public void login(User user) {
        this.currentUser = user;
    }

    /**
     * Clears the active session state.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Retrieves the currently authenticated user.
     * @return The active user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks whether a user is currently authenticated.
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}