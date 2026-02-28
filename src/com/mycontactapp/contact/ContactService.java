package com.mycontactapp.contact;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.model.User;
import com.mycontactapp.util.FileHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ContactService
 * Manages core contact operations such as creation and data retrieval.
 *
 * @author Developer
 * @version 2.0
 */
public class ContactService {
    
    private final List<Contact> allContacts;

    public ContactService() {
        this.allContacts = FileHandler.loadContacts();
    }

    public Person createPersonContact(User owner, String name, String phone, String email, String relationship) throws ContactAppException {
        enforceContactLimit(owner);
        Person person = new Person(owner.getUserId(), name, relationship);
        if (!phone.isEmpty()) person.addPhoneNumber(phone);
        if (!email.isEmpty()) person.addEmailAddress(email);
        
        allContacts.add(person);
        FileHandler.saveContacts(allContacts); 
        return person;
    }

    public Organization createOrganizationContact(User owner, String name, String phone, String email, String website) throws ContactAppException {
        enforceContactLimit(owner);
        Organization org = new Organization(owner.getUserId(), name, website);
        if (!phone.isEmpty()) org.addPhoneNumber(phone);
        if (!email.isEmpty()) org.addEmailAddress(email);
        
        allContacts.add(org);
        FileHandler.saveContacts(allContacts); 
        return org;
    }

    /**
     * Retrieves all contacts belonging to a specific user.
     * Demonstrates use of Java Streams API for filtering.
     *
     * @param owner The user requesting their contacts
     * @return A list of the user's contacts
     */
    public List<Contact> getUserContacts(User owner) {
        return allContacts.stream()
                .filter(contact -> contact.getUserId().equals(owner.getUserId()))
                .collect(Collectors.toList());
    }

    private void enforceContactLimit(User owner) throws ContactAppException {
        long currentCount = getUserContacts(owner).size();
        if (currentCount >= owner.getContactLimit()) {
            throw new ContactAppException("Contact limit reached for your account type. Upgrade to Premium!");
        }
    }
}