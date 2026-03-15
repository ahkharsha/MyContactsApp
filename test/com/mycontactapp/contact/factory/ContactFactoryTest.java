package com.mycontactapp.contact.factory;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Organization;
import com.mycontactapp.contact.Person;
import com.mycontactapp.contact.builder.ContactBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactFactoryTest {

    private ContactFactory factory;

    @BeforeEach
    public void setUp() {
        factory = new ContactFactory();
    }

    @Test
    public void testCreatePersonContact() {
        System.out.println("Running Test: ContactFactory creates Person contact");
        ContactBuilder builder = new ContactBuilder()
            .setUserId("user-123")
            .setName("John Doe")
            .setRelationship("Friend")
            .addPhoneNumber("555-1234")
            .addEmailAddress("john@example.com");

        Contact contact = factory.createContact("Person", builder);

        assertTrue(contact instanceof Person, "Created contact should be instance of Person");
        Person person = (Person) contact;
        assertEquals("John Doe", person.getName());
        assertEquals("Friend", person.getRelationship());
        assertTrue(person.getPhoneNumbers().contains("555-1234"));
        assertTrue(person.getEmailAddresses().contains("john@example.com"));
        assertNotNull(person.getContactId());
        System.out.println("Test Passed. Created: Person");
    }

    @Test
    public void testCreateOrganizationContact() {
        System.out.println("Running Test: ContactFactory creates Organization contact");
        ContactBuilder builder = new ContactBuilder()
            .setUserId("user-123")
            .setName("Tech Corp")
            .setWebsite("http://techcorp.com")
            .addPhoneNumber("555-9876");

        Contact contact = factory.createContact("Organization", builder);

        assertTrue(contact instanceof Organization, "Created contact should be instance of Organization");
        Organization org = (Organization) contact;
        assertEquals("Tech Corp", org.getName());
        assertEquals("http://techcorp.com", org.getWebsite());
        assertTrue(org.getPhoneNumbers().contains("555-9876"));
        assertTrue(org.getEmailAddresses().isEmpty());
        System.out.println("Test Passed. Created: Organization");
    }

    @Test
    public void testUnsupportedContactType() {
        System.out.println("Running Test: ContactFactory rejects unsupported contact type");
        ContactBuilder builder = new ContactBuilder().setName("Unknown Entity");

        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class, 
            () -> factory.createContact("Alien", builder),
            "Expected createContact() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Unsupported contact type: Alien"));
        System.out.println("Test Passed.");
    }

    @Test
    public void testCreateExistingPersonContact() {
        System.out.println("Running Test: ContactFactory constructs existing Person contact");
        // When loaded from file, contactId already exists
        ContactBuilder builder = new ContactBuilder()
            .setContactId("existing-id-456")
            .setUserId("user-123")
            .setName("Jane Smith")
            .setRelationship("Colleague");

        Contact contact = factory.createContact("Person", builder);

        assertTrue(contact instanceof Person);
        assertEquals("existing-id-456", contact.getContactId());
        assertEquals("Jane Smith", contact.getName());
        assertEquals("Colleague", ((Person) contact).getRelationship());
        System.out.println("Test Passed.");
    }
}
