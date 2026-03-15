package com.mycontactapp.contact.decorator;

import com.mycontactapp.contact.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactDecoratorTest {
    
    private ContactDisplay testContact;

    @BeforeEach
    public void setUp() {
        Person person = new Person("user1", "John Doe", "Friend");
        person.addPhoneNumber("555-1234");
        person.addEmailAddress("john.doe@example.com");
        testContact = person;
    }

    @Test
    public void testUpperCaseDecorator() {
        System.out.println("Running Test: UpperCaseDecorator converts text to uppercase");
        ContactDisplay upperCaseContact = new UpperCaseDecorator(testContact);
        String details = upperCaseContact.getFormattedDetails();
        assertTrue(details.contains("JOHN DOE"));
        assertTrue(details.contains("FRIEND"));
        System.out.println("Test Passed.");
    }

    @Test
    public void testMaskedEmailDecorator() {
        System.out.println("Running Test: MaskedEmailDecorator masks email addresses");
        ContactDisplay maskedEmailContact = new MaskedEmailDecorator(testContact);
        String details = maskedEmailContact.getFormattedDetails();
        assertTrue(details.contains("j******e@example.com"));
        assertFalse(details.contains("john.doe@example.com"));
        System.out.println("Test Passed.");
    }

    @Test
    public void testCombinedDecorators() {
        System.out.println("Running Test: Combined decorators format text properly");
        ContactDisplay combinedContact = new UpperCaseDecorator(new MaskedEmailDecorator(testContact));
        String details = combinedContact.getFormattedDetails();
        assertTrue(details.contains("JOHN DOE")); // Upper case applied
        assertTrue(details.contains("J******E@EXAMPLE.COM")); // Masked and upper cased
        System.out.println("Test Passed.");
    }
}
