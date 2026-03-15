package com.mycontactapp.search.chain;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Person;
import com.mycontactapp.search.SearchFilterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SearchChainTest {

    private Contact contact;
    private SearchHandler chainHead;

    @BeforeEach
    public void setUp() {
        contact = new Person("1", "u1", "Charlie Test", new ArrayList<>(), new ArrayList<>(), LocalDateTime.now(), "Brother", 0, true);
        contact.addPhoneNumber("999-888-7777");
        contact.addEmailAddress("charlie@email.com");
        
        SearchHandler nameHandler = new SearchFilterService.NameMatchHandler();
        SearchHandler phoneHandler = new SearchFilterService.PhoneMatchHandler();
        SearchHandler emailHandler = new SearchFilterService.EmailMatchHandler();
        
        nameHandler.setNext(phoneHandler).setNext(emailHandler);
        chainHead = nameHandler;
    }

    @Test
    public void testChainMatchesName() {
        System.out.println("Running Test: Chain of Responsibility matches Name");
        assertTrue(chainHead.matches(contact, "Charlie"), "Should match name handler");
        System.out.println("Test Passed.");
    }

    @Test
    public void testChainCascadesToPhone() {
        System.out.println("Running Test: Chain cascades to Phone Match Handler");
        // "888" is not in the name, but is in the phone number
        assertTrue(chainHead.matches(contact, "888"), "Should pass name handler and match phone handler");
        System.out.println("Test Passed.");
    }

    @Test
    public void testChainCascadesToEmail() {
        System.out.println("Running Test: Chain cascades to Email Match Handler");
        // "email.com" is not in name or phone, but in email
        assertTrue(chainHead.matches(contact, "email.com"), "Should cascade to email handler and match");
        System.out.println("Test Passed.");
    }
    
    @Test
    public void testChainFailsIfNoMatch() {
        System.out.println("Running Test: Chain fails if no handler matches");
        assertFalse(chainHead.matches(contact, "nomatchstring"), "Should return false if reached end of chain without match");
        System.out.println("Test Passed.");
    }
}
