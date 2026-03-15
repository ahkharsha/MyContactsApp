package com.mycontactapp.user.command;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

/**
 * UpdateEmailCommand
 * Concrete command to update a user's email address.
 */
public class UpdateEmailCommand implements UserCommand {
    private final UserService userService;
    private final User user;
    private final String newEmail;

    public UpdateEmailCommand(UserService userService, User user, String newEmail) {
        this.userService = userService;
        this.user = user;
        this.newEmail = newEmail;
    }

    @Override
    public void execute() throws ContactAppException {
        userService.updateUserEmail(user, newEmail);
    }
}
