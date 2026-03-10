package com.mycontactapp.user;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.builder.UserBuilder;
import com.mycontactapp.user.factory.UserFactory;
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
 * @version 2.0
 */
public class UserService {

    private final List<User> registeredUsers;
    private final UserFactory userFactory;

    /**
     * Constructs a new UserService and loads existing users from file.
     */
    public UserService() {
        this.registeredUsers = FileHandler.loadUsers(); // Load from file!
        this.userFactory = new UserFactory();
    }

    /**
     * Registers a new user with the system.
     * @param email The email address (must be unique)
     * @param password The user's password (will be hashed)
     * @param fullName The user's full name
     * @param isPremium true if the user wants a Premium account, false for Free
     * @return The newly registered User object
     * @throws ContactAppException if validation fails (e.g., duplicated email, weak password)
     */
    public User registerUser(String email, String password, String fullName, boolean isPremium) throws ContactAppException {
        if (!UserValidator.isValidEmail(email)) throw new ContactAppException("Invalid email format provided.");
        if (!UserValidator.isValidPassword(password)) throw new ContactAppException("Password must be at least 6 characters long.");
        if (isEmailTaken(email)) throw new ContactAppException("An account with this email already exists.");

        String hashedPassword = hashPassword(password);
        // Builder assembles the registration payload before the factory selects the concrete subtype.
        UserBuilder builder = new UserBuilder()
            .setEmail(email)
            .setPasswordHash(hashedPassword)
            .setFullName(fullName);
        String accountType = isPremium ? "Premium" : "Free";
        User newUser = userFactory.createUser(accountType, builder);

        registeredUsers.add(newUser);
        FileHandler.saveUsers(registeredUsers); // Save to file
        return newUser;
    }

    /**
     * Updates an existing user's profile information.
     * @param user The user to update
     * @param newName The new full name
     * @throws ContactAppException if the new name is invalid
     */
    public void updateUserProfile(User user, String newName) throws ContactAppException {
        if (newName == null || newName.trim().isEmpty()) throw new ContactAppException("Name cannot be null or empty.");
        user.setFullName(newName);
        FileHandler.saveUsers(registeredUsers);
    }

    /**
     * Updates a user's email address.
     * @param user The user
     * @param newEmail The new email
     * @throws ContactAppException if invalid or taken
     */
    public void updateUserEmail(User user, String newEmail) throws ContactAppException {
        if (!UserValidator.isValidEmail(newEmail)) throw new ContactAppException("Invalid email format.");
        if (isEmailTaken(newEmail)) throw new ContactAppException("Email already in use.");
        
        user.setEmail(newEmail);
        FileHandler.saveUsers(registeredUsers);
    }

    /**
     * Deletes a user account.
     * @param user The user to delete
     * @return true if successful
     */
    public boolean deleteUserAccount(User user) {
        boolean removed = registeredUsers.remove(user);
        if (removed) FileHandler.saveUsers(registeredUsers);
        return removed;
    }

    /**
     * Checks if the provided password matches the user's current password.
     * @param user The user attempting to authenticate
     * @param currentPassword The password to check
     * @throws ContactAppException if the password does not match
     */
    public void verifyCurrentPassword(User user, String currentPassword) throws ContactAppException {
        String hashedCurrent = hashPassword(currentPassword);
        if (!user.getPasswordHash().equals(hashedCurrent)) {
            throw new ContactAppException("Security Error: Current password is incorrect.");
        }
    }

    /**
     * Updates the password AFTER it has already been verified.
     * @param user        The user whose password will be changed
     * @param newPassword The new password to set
     * @throws ContactAppException if the new password is too short or invalid
     */
    public void changeUserPassword(User user, String newPassword) throws ContactAppException {
        if (!UserValidator.isValidPassword(newPassword)) {
            throw new ContactAppException("New password must be at least 6 characters long.");
        }
        user.setPasswordHash(hashPassword(newPassword));
        FileHandler.saveUsers(registeredUsers); // Save to file!
    }

    /**
     * Retrieves a user by their email address.
     * @param email The email address to search for
     * @return An Optional containing the User if found, or empty otherwise
     */
    public Optional<User> getUserByEmail(String email) {
        return registeredUsers.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    /**
     * Checks if an email address is already registered in the system.
     * @param email The email address to check
     * @return true if the email is already taken, false otherwise
     */
    private boolean isEmailTaken(String email) {
        return registeredUsers.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Hashes a plain text password using SHA-256 for security.
     * @param plainText The raw password
     * @return The hex-encoded hash string
     */
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