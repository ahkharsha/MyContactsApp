package com.mycontactapp.user.command;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileUpdateCommandTest {

    private UserService userService;
    private User testUser;
    private ProfileUpdateInvoker invoker;

    @BeforeEach
    public void setUp() throws Exception {
        userService = new UserService();
        // Using a unique email for tests to prevent conflicts in the file persistence
        String uniqueEmail = "testcmd_" + System.currentTimeMillis() + "@example.com";
        testUser = userService.registerUser(uniqueEmail, "password123", "Test User", false);
        invoker = new ProfileUpdateInvoker();
    }

    @Test
    public void testUpdateNameCommand() throws ContactAppException {
        UserCommand command = new UpdateNameCommand(userService, testUser, "New Name");
        invoker.executeCommand(command);
        assertEquals("New Name", testUser.getFullName(), "User name should be updated via command.");
    }

    @Test
    public void testUpdateEmailCommand() throws ContactAppException {
        String newUniqueEmail = "newcmd_" + System.currentTimeMillis() + "@example.com";
        UserCommand command = new UpdateEmailCommand(userService, testUser, newUniqueEmail);
        invoker.executeCommand(command);
        assertEquals(newUniqueEmail, testUser.getEmail(), "User email should be updated via command.");
    }

    @Test
    public void testChangePasswordCommand() throws ContactAppException {
        UserCommand command = new ChangePasswordCommand(userService, testUser, "password123", "newpass456");
        invoker.executeCommand(command);
        assertDoesNotThrow(() -> userService.verifyCurrentPassword(testUser, "newpass456"), "New password should be valid.");
        assertThrows(ContactAppException.class, () -> userService.verifyCurrentPassword(testUser, "password123"), "Old password should be invalid.");
    }
}
