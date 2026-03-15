package com.mycontactapp.search.specification;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Person;
import com.mycontactapp.search.SearchFilterService;
import com.mycontactapp.tagging.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SpecificationTest {

    private Contact contact1;
    private Contact contact2;

    @BeforeEach
    public void setUp() {
        contact1 = new Person("1", "u1", "Testing Alice", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Friend", 0, true);
        contact1.addTag(new Tag("work"));
        contact1.addPhoneNumber("555-1234");
        
        contact2 = new Person("2", "u1", "Bob Smith", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Colleague", 0, true);
        contact2.addTag(new Tag("family"));
        contact2.addEmailAddress("bob@test.com");
    }

    @Test
    public void testAndSpecification() {
        System.out.println("Running Test: AndSpecification requires both to match");
        Specification nameSpec = new SearchFilterService.NameSearch();
        Specification tagSpec = new SearchFilterService.TagSearch();
        
        Specification andSpec = (Specification) nameSpec.and(tagSpec);
        
        // With a combined Specification where both check the SAME query string, it would only match if the query satisfies BOTH.
        // E.g., if the user's name is "work" and tag is "work", then searching for "work" matches both.
        // For our test objects, searching "alice" matches Name but fails Tag.
        assertFalse(andSpec.matches(contact1, "alice"), "Should be false because tag is not 'alice'");
        
        // To properly test our anonymous class pattern from the menu:
        boolean match1 = nameSpec.matches(contact1, "Alice") && tagSpec.matches(contact1, "work");
        assertTrue(match1, "Should match both conditions individually");
        
        boolean match2 = nameSpec.matches(contact1, "Alice") && tagSpec.matches(contact1, "family");
        assertFalse(match2, "Should fail if one condition fails");
        System.out.println("Test Passed.");
    }

    @Test
    public void testOrSpecification() {
        System.out.println("Running Test: OrSpecification requires at least one match");
        Specification nameSpec = new SearchFilterService.NameSearch();
        Specification emailSpec = new SearchFilterService.EmailSearch();
        
        Specification orSpec = (Specification) nameSpec.or(emailSpec);
        
        // This is tricky because the `matches` takes a single query String. 
        // If we pass "bob", nameSpec matches. 
        assertTrue(orSpec.matches(contact2, "bob"), "Name spec should match 'bob'");
        assertTrue(orSpec.matches(contact2, "test.com"), "Email spec should match 'test.com'");
        assertFalse(orSpec.matches(contact2, "nomatch"), "Neither should match 'nomatch'");
        System.out.println("Test Passed.");
    }
}
