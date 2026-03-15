package com.mycontactapp.user.command;

import com.mycontactapp.exception.ContactAppException;

/**
 * UserCommand interface
 * Represents a generic command to be executed in the user profile context.
 */
public interface UserCommand {
    void execute() throws ContactAppException;
}
