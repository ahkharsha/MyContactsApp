package com.mycontactapp.user.command;

import com.mycontactapp.exception.ContactAppException;

/**
 * ProfileUpdateInvoker
 * Invokes and optionally tracks commands for the user profile.
 */
public class ProfileUpdateInvoker {
    
    public void executeCommand(UserCommand command) throws ContactAppException {
        command.execute();
    }
}
