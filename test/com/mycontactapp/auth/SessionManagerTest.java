package com.mycontactapp.auth;

import com.mycontactapp.user.model.FreeUser;
import com.mycontactapp.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SessionManagerTest
 * Verifies singleton session behavior for UC2 authentication state management.
 */
class SessionManagerTest {

    private final SessionManager sessionManager = SessionManager.getInstance();

    @BeforeEach
    void resetSession() {
        sessionManager.logout();
    }

    @Test
    void getInstanceReturnsSameSingletonObject() {
        System.out.println("Running Test: SessionManager.getInstance() returns the same singleton object");
        SessionManager anotherReference = SessionManager.getInstance();

        assertSame(sessionManager, anotherReference);
        System.out.println("Test Passed.");
    }

    @Test
    void loginStoresAuthenticatedUserInSession() {
        System.out.println("Running Test: SessionManager.login() stores authenticated user");
        User user = new FreeUser("alice@example.com", "hashed-password", "Alice");

        sessionManager.login(user);

        assertTrue(sessionManager.isLoggedIn());
        assertSame(user, sessionManager.getCurrentUser());
        System.out.println("Test Passed.");
    }

    @Test
    void logoutClearsAuthenticatedSessionState() {
        System.out.println("Running Test: SessionManager.logout() clears session state");
        User user = new FreeUser("bob@example.com", "hashed-password", "Bob");
        sessionManager.login(user);

        sessionManager.logout();

        assertFalse(sessionManager.isLoggedIn());
        assertNull(sessionManager.getCurrentUser());
        System.out.println("Test Passed.");
    }
}