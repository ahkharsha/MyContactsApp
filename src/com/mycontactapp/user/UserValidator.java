package com.mycontactapp.user;

import java.util.regex.Pattern;

/**
 * UserValidator
 * Helpers to check if email and password formats are correct.
 *
 * @author Developer
 * @version 1.0
 */
public class UserValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Validates if the provided string is a properly formatted email address.
     *
     * @param email The email string to check
     * @return true if the email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if the password meets minimum security requirements.
     * (e.g., minimum 6 characters).
     *
     * @param password The raw password string to check
     * @return true if the password is secure, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}