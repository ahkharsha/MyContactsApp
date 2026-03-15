package com.mycontactapp.contact.command;

import java.util.Stack;

/**
 * ContactEditInvoker
 * Manages the execution, undoing, and redoing of contact edits 
 * using Command objects and two operational stacks.
 */
public class ContactEditInvoker {
    private final Stack<EditContactCommand> undoStack = new Stack<>();
    private final Stack<EditContactCommand> redoStack = new Stack<>();

    /**
     * Executes a new command and pushes it to the undo stack.
     * Clears the redo stack because history has branched.
     * @param command The command to execute
     */
    public void executeCommand(EditContactCommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    /**
     * Undoes the most recently executed command.
     * @return true if an undo occurred, false otherwise
     */
    public boolean undoLastCommand() {
        if (!undoStack.isEmpty()) {
            EditContactCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            return true;
        }
        return false;
    }

    /**
     * Redoes the most recently undone command.
     * @return true if a redo occurred, false otherwise
     */
    public boolean redoLastCommand() {
        if (!redoStack.isEmpty()) {
            EditContactCommand command = redoStack.pop();
            command.redo();
            undoStack.push(command);
            return true;
        }
        return false;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
