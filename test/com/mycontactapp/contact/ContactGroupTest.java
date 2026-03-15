package com.mycontactapp.contact;

import com.mycontactapp.tagging.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactGroupTest {

    private Contact c1;
    private Contact c2;
    private ContactGroup group;

    @BeforeEach
    public void setUp() {
        c1 = new Person("1", "u1", "Alice", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Friend", 0, true);
        c2 = new Organization("2", "u1", "Bob Corp", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "bob.com", 0, true);
        
        group = new ContactGroup("Test Group");
        group.add(c1);
        group.add(c2);
    }

    @Test
    public void testAddTagToGroup() {
        System.out.println("Running Test: ContactGroup delegates addTag to all children");
        Tag newTag = new Tag("NewTag");
        group.addTag(newTag);
        
        assertTrue(c1.getTags().contains(newTag), "Child 1 should have the tag");
        assertTrue(c2.getTags().contains(newTag), "Child 2 should have the tag");
        System.out.println("Test Passed.");
    }

    @Test
    public void testRemoveTagFromGroup() {
        System.out.println("Running Test: ContactGroup delegates removeTag to all children");
        Tag tag = new Tag("TempTag");
        c1.addTag(tag);
        c2.addTag(tag);
        
        group.removeTag(tag);
        assertFalse(c1.getTags().contains(tag), "Child 1 should not have the tag");
        assertFalse(c2.getTags().contains(tag), "Child 2 should not have the tag");
        System.out.println("Test Passed.");
    }

    @Test
    public void testSetActiveOnGroup() {
        System.out.println("Running Test: ContactGroup delegates setActive to all children");
        group.setActive(false);
        assertFalse(c1.isActive(), "Child 1 should be inactive");
        assertFalse(c2.isActive(), "Child 2 should be inactive");
        
        group.setActive(true);
        assertTrue(c1.isActive(), "Child 1 should be active again");
        assertTrue(c2.isActive(), "Child 2 should be active again");
        System.out.println("Test Passed.");
    }

    @Test
    public void testGetAsContactList() {
        System.out.println("Running Test: ContactGroup flattens to a List<Contact>");
        List<Contact> flatList = group.getAsContactList();
        assertEquals(2, flatList.size(), "List should contain exactly 2 contacts");
        assertTrue(flatList.contains(c1), "List should contain c1");
        assertTrue(flatList.contains(c2), "List should contain c2");
        System.out.println("Test Passed.");
    }
}
