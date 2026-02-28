package com.mycontactapp.user;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.model.FreeUser;
import com.mycontactapp.user.model.PremiumUser;
import com.mycontactapp.user.model.User;
import com.mycontactapp.util.FileHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

/**
 * UserService
 * Handles user-related tasks like registering new accounts.
 *
 * @author Developer
 * @version 4.0
 */
public class UserService {

    private final List<User> registeredUsers;

    public UserService() {
        this.registeredUsers = FileHandler.loadUsers(); // Load from file!
    }

    public User registerUser(String email, String password, String fullName, boolean isPremium) throws ContactAppException {
        if (!UserValidator.isValidEmail(email)) throw new ContactAppException("Invalid email format provided.");
        if (!UserValidator.isValidPassword(password)) throw new ContactAppException("Password must be at least 6 characters long.");
        if (isEmailTaken(email)) throw new ContactAppException("An account with this email already exists.");

        String hashedPassword = hashPassword(password);
        User newUser = isPremium ? new PremiumUser(email, hashedPassword, fullName) : new FreeUser(email, hashedPassword, fullName);

        registeredUsers.add(newUser);
        FileHandler.saveUsers(registeredUsers); // Save to file!
        return newUser;
    }

    public void updateUserProfile(User user, String newName) throws ContactAppException {
        if (newName == null || newName.trim().isEmpty()) throw new ContactAppException("Name cannot be null or empty.");
        user.setFullName(newName);
        FileHandler.saveUsers(registeredUsers);
    }

    /**
     * Checks if the provided password matches the user's current password.
     */
    public void verifyCurrentPassword(User user, String currentPassword) throws ContactAppException {
        String hashedCurrent = hashPassword(currentPassword);
        if (!user.getPasswordHash().equals(hashedCurrent)) {
            throw new ContactAppException("Security Error: Current password is incorrect.");
        }
    }

    /**
     * Updates the password AFTER it has already been verified.
     */
    public void changeUserPassword(User user, String newPassword) throws ContactAppException {
        if (!UserValidator.isValidPassword(newPassword)) {
            throw new ContactAppException("New password must be at least 6 characters long.");
        }
        user.setPasswordHash(hashPassword(newPassword));
        FileHandler.saveUsers(registeredUsers); // Save to file!
    }

    public Optional<User> getUserByEmail(String email) {
        return registeredUsers.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    private boolean isEmailTaken(String email) {
        return registeredUsers.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    private String hashPassword(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(plainText.getBytes());
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algorithm not found.", e);
        }
    }
}