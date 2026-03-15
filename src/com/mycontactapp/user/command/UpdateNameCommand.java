package com.mycontactapp.user.command;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

/**
 * UpdateNameCommand
 * Concrete command to update a user's full name.
 */
public class UpdateNameCommand implements UserCommand {
    private final UserService userService;
    private final User user;
    private final String newName;

    public UpdateNameCommand(UserService userService, User user, String newName) {
        this.userService = userService;
        this.user = user;
        this.newName = newName;
    }

    @Override
    public void execute() throws ContactAppException {
        userService.updateUserProfile(user, newName);
    }
}
