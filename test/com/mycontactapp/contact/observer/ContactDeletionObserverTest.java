package com.mycontactapp.contact.observer;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.user.model.FreeUser;
import com.mycontactapp.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactDeletionObserverTest {

    private ContactService contactService;
    private User testUser;
    private Contact testContact;

    // A mock observer to track if events were fired during tests
    private static class MockObserver implements ContactDeletionObserver {
        boolean wasCalled = false;
        boolean wasHardDelete = false;

        @Override
        public void onContactDeleted(Contact contact, boolean isHardDelete) {
            wasCalled = true;
            wasHardDelete = isHardDelete;
            System.out.println("MockObserver received " + (isHardDelete ? "HARD" : "SOFT") + " delete notification for " + contact.getName());
        }
    }

    @BeforeEach
    public void setUp() {
        contactService = new ContactService();
        testUser = new FreeUser("testuser1", "test@example.com", "hash", "Test User");

        try {
            testContact = contactService.createPersonContact(testUser, "Observer Test Target", "555-9999", "del@example.com", "Friend");
        } catch (Exception e) {
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    public void testObserverIsNotifiedOnSoftDelete() {
        System.out.println("Running Test: Observer notified on Soft Delete");
        MockObserver mockObserver = new MockObserver();
        contactService.addObserver(mockObserver);

        contactService.deleteContact(testContact, false);

        assertTrue(mockObserver.wasCalled, "Observer should be called upon deletion");
        assertFalse(mockObserver.wasHardDelete, "Deletion should be marked as soft");
        assertFalse(testContact.isActive(), "Contact should be marked inactive");
        
        // In a soft delete, it theoretically remains in storage but is filtered by getUserContacts
        List<Contact> filteredContacts = contactService.getUserContacts(testUser);
        assertFalse(filteredContacts.contains(testContact), "Soft deleted contact should not be returned by getUserContacts");
        System.out.println("Test Passed.");
    }

    @Test
    public void testObserverIsNotifiedOnHardDelete() {
        System.out.println("Running Test: Observer notified on Hard Delete");
        MockObserver mockObserver = new MockObserver();
        contactService.addObserver(mockObserver);

        contactService.deleteContact(testContact, true);

        assertTrue(mockObserver.wasCalled, "Observer should be called upon deletion");
        assertTrue(mockObserver.wasHardDelete, "Deletion should be marked as hard");
        
        List<Contact> filteredContacts = contactService.getUserContacts(testUser);
        assertFalse(filteredContacts.contains(testContact), "Hard deleted contact should be fully removed");
        System.out.println("Test Passed.");
    }
}
