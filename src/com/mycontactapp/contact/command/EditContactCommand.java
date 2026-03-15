package com.mycontactapp.contact.command;

/**
 * EditContactCommand
 * Interface defining the execution structure for contact modifications,
 * incorporating Undo and Redo capabilities.
 *
 * @author Developer
 * @version 1.0
 */
public interface EditContactCommand {
    /**
     * Executes the contact modification and creates a snapshot for undo.
     */
    void execute();

    /**
     * Reverts the contact to its state exactly before the command was executed.
     */
    void undo();

    /**
     * Reapplies the contact modification that was previously undone.
     */
    void redo();
}
