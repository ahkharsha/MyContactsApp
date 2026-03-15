package com.mycontactapp.tagging.observer;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.contact.Person;
import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.tagging.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TagObserverTest {

    private ContactService contactService;
    private MockTagObserver observer;
    private Contact testContact;

    // A simple mock observer to verify that events are firing correctly
    private static class MockTagObserver implements TagChangeObserver {
        public boolean addedFired = false;
        public boolean removedFired = false;
        public Contact lastContact = null;
        public Tag lastTag = null;

        @Override
        public void onTagAdded(Contact contact, Tag tag) {
            addedFired = true;
            lastContact = contact;
            lastTag = tag;
        }

        @Override
        public void onTagRemoved(Contact contact, Tag tag) {
            removedFired = true;
            lastContact = contact;
            lastTag = tag;
        }
        
        public void reset() {
            addedFired = false;
            removedFired = false;
            lastContact = null;
            lastTag = null;
        }
    }

    @BeforeEach
    public void setUp() {
        contactService = new ContactService();
        observer = new MockTagObserver();
        
        // Register our mock observer
        contactService.addTagObserver(observer);
        
        // Setup a dummy contact
        testContact = new Person("1", "u1", "Subject Contact", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Friend", 0, true);
    }

    @Test
    public void testObserverFiredOnTagAdd() throws ContactAppException {
        System.out.println("Running Test: Observer is notified when Tag is Added");
        observer.reset();
        
        contactService.addTagToContact(testContact, "work");
        
        assertTrue(observer.addedFired, "The added event should have fired");
        assertFalse(observer.removedFired, "The removed event should NOT have fired");
        assertEquals(testContact, observer.lastContact, "The contact passed to the observer should match");
        assertEquals("work", observer.lastTag.getName(), "The tag passed to the observer should match");
        
        System.out.println("Test Passed.");
    }

    @Test
    public void testObserverFiredOnTagRemove() throws ContactAppException {
        System.out.println("Running Test: Observer is notified when Tag is Removed");
        contactService.addTagToContact(testContact, "school");
        observer.reset(); // clear the add
        
        contactService.removeTagFromContact(testContact, "school");
        
        assertTrue(observer.removedFired, "The removed event should have fired");
        assertFalse(observer.addedFired, "The added event should NOT have fired");
        assertEquals("school", observer.lastTag.getName(), "The tag passed to the observer should match");
        
        System.out.println("Test Passed.");
    }
}
