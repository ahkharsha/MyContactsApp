package com.mycontactapp.user;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.model.FreeUser;
import com.mycontactapp.user.model.PremiumUser;
import com.mycontactapp.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UserService
 * Handles user-related tasks like registering new accounts.
 *
 * @author Developer
 * @version 1.0
 */
public class UserService {

    // In-memory storage for UC1 until File I/O is integrated
    private final List<User> registeredUsers;

    /**
     * Constructs a new UserService and initializes the user list.
     */
    public UserService() {
        this.registeredUsers = new ArrayList<>();
    }

    /**
     * Registers a new user after validating their inputs and hashing their password.
     *
     * @param email     The desired email address
     * @param password  The raw, unhashed password
     * @param fullName  The user's real name
     * @param isPremium Boolean flag to determine the account tier
     * @return The newly created User object
     * @throws ContactAppException if validation fails or the email is already taken
     */
    public User registerUser(String email, String password, String fullName, boolean isPremium) throws ContactAppException {
        if (!UserValidator.isValidEmail(email)) {
            throw new ContactAppException("Invalid email format provided.");
        }
        if (!UserValidator.isValidPassword(password)) {
            throw new ContactAppException("Password must be at least 6 characters long.");
        }
        if (isEmailTaken(email)) {
            throw new ContactAppException("An account with this email already exists.");
        }

        String hashedPassword = hashPassword(password);
        User newUser;

        // Instantiate using standard OOP rather than a Factory Pattern
        if (isPremium) {
            newUser = new PremiumUser(email, hashedPassword, fullName);
        } else {
            newUser = new FreeUser(email, hashedPassword, fullName);
        }

        registeredUsers.add(newUser);
        return newUser;
    }

    /**
     * Checks if an email is already registered in the system.
     * Demonstrates the use of the Java Streams API.
     *
     * @param email The email to check
     * @return true if the email exists, false otherwise
     */
    private boolean isEmailTaken(String email) {
        return registeredUsers.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Hashes a raw password string securely using SHA-256.
     *
     * @param plainTextPassword The raw password
     * @return The hex-encoded hashed password
     * @throws ContactAppException if the hashing algorithm is unavailable
     */
    private String hashPassword(String plainTextPassword) throws ContactAppException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(plainTextPassword.getBytes());
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
            throw new ContactAppException("Critical Security Error: Hash algorithm not found.");
        }
    }
    
    /**
     * Retrieves all currently registered users.
     * * @return A List containing all registered User objects.
     */
    public List<User> getRegisteredUsers() {
        return new ArrayList<>(registeredUsers); // Return a copy to protect internal state
    }
    
    /**
     * Searches for a registered user by their email address safely.
     *
     * @param email The email to search for
     * @return An Optional containing the User if found, or empty if not found
     */
    public Optional<User> getUserByEmail(String email) {
        return registeredUsers.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}