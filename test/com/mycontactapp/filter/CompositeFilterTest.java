package com.mycontactapp.filter;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Person;
import com.mycontactapp.tagging.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompositeFilterTest {

    private Contact c1;
    private Contact c2;
    private Contact c3;
    private List<Contact> contactList;

    @BeforeEach
    public void setUp() {
        contactList = new ArrayList<>();
        
        // c1: work tag, 10 views
        c1 = new Person("1", "u1", "Alice", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Notes", 10, true);
        c1.addTag(new Tag("work"));
        
        // c2: family tag, 2 views
        c2 = new Person("2", "u1", "Bob", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Notes", 2, true);
        c2.addTag(new Tag("family"));

        // c3: work tag, 0 views
        c3 = new Person("3", "u1", "Charlie", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Notes", 0, true);
        c3.addTag(new Tag("work"));

        contactList.add(c1);
        contactList.add(c2);
        contactList.add(c3);
    }

    @Test
    public void testTagFilterStrategy() {
        System.out.println("Running Test: TagFilter Strategy isolates contacts by exact tag match");
        ContactFilter workFilter = new TagFilter("work");
        List<Contact> results = workFilter.filter(contactList);
        
        assertEquals(2, results.size(), "Should find 2 work contacts");
        assertTrue(results.contains(c1), "Should contain Alice");
        assertTrue(results.contains(c3), "Should contain Charlie");
        assertFalse(results.contains(c2), "Should NOT contain Bob");
        System.out.println("Test Passed.");
    }

    @Test
    public void testFrequentlyContactedFilterStrategy() {
        System.out.println("Running Test: FrequentlyContactedFilter Strategy filters by viewCount threshold");
        ContactFilter viewFilter = new FrequentlyContactedFilter(5);
        List<Contact> results = viewFilter.filter(contactList);
        
        assertEquals(1, results.size(), "Should find 1 contact with >= 5 views");
        assertTrue(results.contains(c1), "Should contain Alice");
        System.out.println("Test Passed.");
    }

    @Test
    public void testCompositeFilterCombinesStrategies() {
        System.out.println("Running Test: CompositeFilter applies multi-level filtering (AND logic)");
        CompositeFilter composite = new CompositeFilter();
        composite.addFilter(new TagFilter("work"));
        composite.addFilter(new FrequentlyContactedFilter(5));
        
        // Expecting: Has "work" tag AND viewCount >= 5. Only c1 matches both.
        List<Contact> results = composite.filter(contactList);
        
        assertEquals(1, results.size(), "Should find exactly 1 contact matching both filters");
        assertTrue(results.contains(c1), "Should contain Alice");
        System.out.println("Test Passed.");
    }
}
