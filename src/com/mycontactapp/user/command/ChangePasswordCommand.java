package com.mycontactapp.user.command;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

/**
 * ChangePasswordCommand
 * Concrete command to alter a user's password.
 */
public class ChangePasswordCommand implements UserCommand {
    private final UserService userService;
    private final User user;
    private final String currentPassword;
    private final String newPassword;

    public ChangePasswordCommand(UserService userService, User user, String currentPassword, String newPassword) {
        this.userService = userService;
        this.user = user;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    @Override
    public void execute() throws ContactAppException {
        userService.verifyCurrentPassword(user, currentPassword);
        userService.changeUserPassword(user, newPassword);
    }
}
