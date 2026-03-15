package com.mycontactapp.contact.command;

import com.mycontactapp.contact.ContactService;
import com.mycontactapp.contact.Person;
import com.mycontactapp.user.model.User;
import com.mycontactapp.user.model.FreeUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactEditCommandTest {

    private ContactService contactService;
    private ContactEditInvoker invoker;
    private Person testContact;

    @BeforeEach
    public void setUp() {
        // We initialize actual services since they interact with FileHandler. 
        // This acts as an integration test for the Command pattern.
        contactService = new ContactService();
        invoker = new ContactEditInvoker();
        User testUser = new FreeUser("testuser1", "test@example.com", "hash", "Test User");

        try {
            testContact = contactService.createPersonContact(testUser, "Original Name", "555-1111", "orig@example.com", "Friend");
        } catch (Exception e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    public void testModifyContactCommandExecutes() {
        System.out.println("Running Test: ModifyContactCommand executes and changes state");
        EditContactCommand command = new ModifyContactCommand(testContact, contactService, c -> c.setName("New Name"));
        invoker.executeCommand(command);

        assertEquals("New Name", testContact.getName());
        System.out.println("Test Passed.");
    }

    @Test
    public void testUndoRestoresPreviousState() {
        System.out.println("Running Test: Undo restores previous contact state");
        EditContactCommand command = new ModifyContactCommand(testContact, contactService, c -> c.setName("Edited Name"));
        invoker.executeCommand(command);

        assertEquals("Edited Name", testContact.getName(), "State should change after execution");

        boolean undoSuccess = invoker.undoLastCommand();
        assertTrue(undoSuccess, "Undo should return true");
        assertEquals("Original Name", testContact.getName(), "State should revert to original name after undo");
        System.out.println("Test Passed.");
    }

    @Test
    public void testRedoReappliesState() {
        System.out.println("Running Test: Redo reapplies contact state");
        EditContactCommand command = new ModifyContactCommand(testContact, contactService, c -> {
            c.setName("Edited Name");
            c.addPhoneNumber("555-2222");
        });
        invoker.executeCommand(command);
        invoker.undoLastCommand();

        assertEquals("Original Name", testContact.getName(), "State should be undone");
        assertFalse(testContact.getPhoneNumbers().contains("555-2222"));

        boolean redoSuccess = invoker.redoLastCommand();
        assertTrue(redoSuccess, "Redo should return true");
        assertEquals("Edited Name", testContact.getName(), "State should reapply after redo");
        assertTrue(testContact.getPhoneNumbers().contains("555-2222"), "New phone should reappear");
        System.out.println("Test Passed.");
    }

    @Test
    public void testCanUndoCanRedoFlags() {
        System.out.println("Running Test: Verify invoker stack flags");
        assertFalse(invoker.canUndo());
        assertFalse(invoker.canRedo());

        EditContactCommand command = new ModifyContactCommand(testContact, contactService, c -> c.setName("Edited Name"));
        invoker.executeCommand(command);

        assertTrue(invoker.canUndo());
        assertFalse(invoker.canRedo());

        invoker.undoLastCommand();

        assertFalse(invoker.canUndo());
        assertTrue(invoker.canRedo());
        System.out.println("Test Passed.");
    }
}
