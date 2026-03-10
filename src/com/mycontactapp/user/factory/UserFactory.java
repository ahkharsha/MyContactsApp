package com.mycontactapp.user.factory;

import com.mycontactapp.user.builder.UserBuilder;
import com.mycontactapp.user.model.FreeUser;
import com.mycontactapp.user.model.PremiumUser;
import com.mycontactapp.user.model.User;

/**
 * UserFactory
 * Creates the correct concrete user subtype from pre-assembled builder data.
 *
 * @author Developer
 * @version 1.0
 */
public class UserFactory {

    /**
     * Creates either a FreeUser or PremiumUser based on the requested account type.
     * @param accountType The requested account type (Free or Premium)
     * @param builder The builder holding the validated user data
     * @return The constructed concrete User subtype
     */
    public User createUser(String accountType, UserBuilder builder) {
        return switch (accountType.trim().toLowerCase()) {
            case "premium" -> createPremiumUser(builder);
            case "free" -> createFreeUser(builder);
            default -> throw new IllegalArgumentException("Unsupported account type: " + accountType);
        };
    }

    /**
     * Creates a FreeUser from builder data.
     * @param builder The builder holding the user data
     * @return A FreeUser instance
     */
    public User createFreeUser(UserBuilder builder) {
        String creationMode = builder.getUserId() == null || builder.getUserId().isBlank() ? "new" : "existing";
        return switch (creationMode) {
            case "new" -> new FreeUser(builder.getEmail(), builder.getPasswordHash(), builder.getFullName());
            case "existing" -> new FreeUser(builder.getEmail(), builder.getPasswordHash(), builder.getFullName(), builder.getUserId());
            default -> throw new IllegalStateException("Unsupported creation mode: " + creationMode);
        };
    }

    /**
     * Creates a PremiumUser from builder data.
     * @param builder The builder holding the user data
     * @return A PremiumUser instance
     */
    public User createPremiumUser(UserBuilder builder) {
        String creationMode = builder.getUserId() == null || builder.getUserId().isBlank() ? "new" : "existing";
        return switch (creationMode) {
            case "new" -> new PremiumUser(builder.getEmail(), builder.getPasswordHash(), builder.getFullName());
            case "existing" -> new PremiumUser(builder.getEmail(), builder.getPasswordHash(), builder.getFullName(), builder.getUserId());
            default -> throw new IllegalStateException("Unsupported creation mode: " + creationMode);
        };
    }
}