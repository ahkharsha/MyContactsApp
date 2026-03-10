package com.mycontactapp.user.factory;

import com.mycontactapp.user.builder.UserBuilder;
import com.mycontactapp.user.model.FreeUser;
import com.mycontactapp.user.model.PremiumUser;
import com.mycontactapp.user.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * UserFactoryTest
 * Verifies UC1 builder and factory behavior for user creation.
 */
class UserFactoryTest {

    private final UserFactory userFactory = new UserFactory();

    @Test
    void createUserReturnsFreeUserForFreeAccountType() {
        UserBuilder builder = new UserBuilder()
                .setEmail("free@example.com")
                .setPasswordHash("hashed-password")
                .setFullName("Free User");

        User user = userFactory.createUser("Free", builder);

        assertInstanceOf(FreeUser.class, user);
        assertEquals("free@example.com", user.getEmail());
        assertEquals("Free User", user.getFullName());
    }

    @Test
    void createUserReturnsPremiumUserForPremiumAccountType() {
        UserBuilder builder = new UserBuilder()
                .setEmail("premium@example.com")
                .setPasswordHash("hashed-password")
                .setFullName("Premium User");

        User user = userFactory.createUser("Premium", builder);

        assertInstanceOf(PremiumUser.class, user);
        assertEquals("premium@example.com", user.getEmail());
        assertEquals("Premium User", user.getFullName());
    }

    @Test
    void createUserPreservesExistingUserIdWhenLoadingPersistedUser() {
        UserBuilder builder = new UserBuilder()
                .setUserId("persisted-123")
                .setEmail("loaded@example.com")
                .setPasswordHash("hashed-password")
                .setFullName("Loaded User");

        User user = userFactory.createUser("Free", builder);

        assertEquals("persisted-123", user.getUserId());
    }

    @Test
    void createUserRejectsUnsupportedAccountType() {
        UserBuilder builder = new UserBuilder()
                .setEmail("invalid@example.com")
                .setPasswordHash("hashed-password")
                .setFullName("Invalid User");

        assertThrows(IllegalArgumentException.class, () -> userFactory.createUser("Admin", builder));
    }
}