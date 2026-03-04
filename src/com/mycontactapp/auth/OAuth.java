package com.mycontactapp.auth;

import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

import java.util.Optional;

/**
 * OAuth
 * Handles login using external accounts like Google or GitHub.
 * This is just a simulation (mock) for now.
 *
 * @author Developer
 * @version 1.0
 */
public class OAuth implements Authentication {

    private final UserService userService;

    /**
     * Constructs OAuth with access to the user repository.
     * @param userService The service containing registered users
     */
    public OAuth(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> authenticate(String email, String provider) {
        System.out.println("\n[System: Contacting " + provider + " servers...]");
        System.out.println("[System: Automatically negotiating secure OAuth token...]");
        
        // Simulating a slight delay for realistic console UX
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("[System: Token received successfully from " + provider + "]");

        // In a real app, the token would securely link to the user's email.
        // Here, we trust the mocked flow and fetch the user.
        return userService.getUserByEmail(email);
    }
}