package com.mycontactapp.contact;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.model.User;
import com.mycontactapp.util.FileHandler;

import java.util.List;

/**
 * ContactService
 * Handles all contact-related tasks, like adding new people or companies.
 *
 * @author Developer
 * @version 1.0
 */
public class ContactService {
    
    private final List<Contact> allContacts;

    /**
     * Constructs a new ContactService.
     * Loads existing contacts from the file system.
     */
    public ContactService() {
        // Automatically load contacts from text file on startup
        this.allContacts = FileHandler.loadContacts();
    }

    /**
     * Creates and saves a new Person contact.
     *
     * @param owner        The user adding the contact
     * @param name         The contact's name
     * @param phone        The contact's phone number
     * @param email        The contact's email address
     * @param relationship The relationship to the user (e.g., Friend)
     * @return The created Person object
     * @throws ContactAppException if logic fails (e.g., limit reached)
     */
    public Person createPersonContact(User owner, String name, String phone, String email, String relationship) throws ContactAppException {
        enforceContactLimit(owner);
        Person person = new Person(owner.getUserId(), name, relationship);
        if (!phone.isEmpty()) person.addPhoneNumber(phone);
        if (!email.isEmpty()) person.addEmailAddress(email);
        
        allContacts.add(person);
        FileHandler.saveContacts(allContacts); // Persist to file
        return person;
    }

    /**
     * Creates and saves a new Organization contact.
     *
     * @param owner   The user adding the contact
     * @param name    The organization's name
     * @param phone   The organization's phone number
     * @param email   The organization's email address
     * @param website The organization's website URL
     * @return The created Organization object
     * @throws ContactAppException if logic fails (e.g., limit reached)
     */
    public Organization createOrganizationContact(User owner, String name, String phone, String email, String website) throws ContactAppException {
        enforceContactLimit(owner);
        Organization org = new Organization(owner.getUserId(), name, website);
        if (!phone.isEmpty()) org.addPhoneNumber(phone);
        if (!email.isEmpty()) org.addEmailAddress(email);
        
        allContacts.add(org);
        FileHandler.saveContacts(allContacts); // Persist to file
        return org;
    }

    /**
     * Ensures the user has not exceeded their account tier limit.
     *
     * @param owner The user to check
     * @throws ContactAppException if the user has reached their maximum allowed contacts
     */
    private void enforceContactLimit(User owner) throws ContactAppException {
        long currentCount = allContacts.stream()
                .filter(c -> c.getUserId().equals(owner.getUserId()))
                .count();
        if (currentCount >= owner.getContactLimit()) {
            throw new ContactAppException("Contact limit reached for your account type. Upgrade to Premium!");
        }
    }
}