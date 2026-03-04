package com.mycontactapp.auth;

import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * BasicAuth
 * Handles login using a standard email and password.
 *
 * @author Developer
 * @version 1.0
 */
public class BasicAuth implements Authentication {

    private final UserService userService;

    /**
     * Constructs BasicAuth with access to the user repository.
     * @param userService The service containing registered users
     */
    public BasicAuth(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userService.getUserByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String hashedInput = hashPassword(password);
            if (user.getPasswordHash().equals(hashedInput)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * Hashes the inputted password to compare against the stored hash.
     * @param password The raw input password
     * @return The hex-encoded hash
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found.", e);
        }
    }
}