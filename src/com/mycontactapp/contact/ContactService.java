package com.mycontactapp.contact;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.model.User;
import com.mycontactapp.util.FileHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ContactService
 * Handles all contact-related tasks, like adding new people or companies.
 * Now supports updating contact details like name, phone, and email.
 *
 * @author Developer
 * @version 4.0
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
     * Finds and returns all contacts created by a specific user.
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
    
    /**
     * Updates the name of a contact.
     *
     * @param contact The contact to update
     * @param newName The new name
     * @throws ContactAppException if name is empty
     */
    public void updateContactName(Contact contact, String newName) throws ContactAppException {
        if (newName == null || newName.trim().isEmpty()) {
            throw new ContactAppException("Contact name cannot be empty.");
        }
        contact.setName(newName);
        FileHandler.saveContacts(allContacts); // Persist changes
    }

    /**
     * Adds a phone number to a contact.
     *
     * @param contact  The contact to update
     * @param newPhone The new phone number
     * @throws ContactAppException if phone is empty
     */
    public void addPhoneToContact(Contact contact, String newPhone) throws ContactAppException {
        if (newPhone == null || newPhone.trim().isEmpty()) {
            throw new ContactAppException("Phone number cannot be empty.");
        }
        contact.addPhoneNumber(newPhone);
        FileHandler.saveContacts(allContacts); // Persist changes
    }

    /**
     * Adds an email address to a contact.
     *
     * @param contact  The contact to update
     * @param newEmail The new email address
     * @throws ContactAppException if email is empty
     */
    public void addEmailToContact(Contact contact, String newEmail) throws ContactAppException {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new ContactAppException("Email address cannot be empty.");
        }
        contact.addEmailAddress(newEmail);
        FileHandler.saveContacts(allContacts); // Persist changes
    }
    
    /**
     * Permanently removes a contact from the system (Hard Delete).
     * Synchronizes the removal with the persistent storage.
     *
     * @param contact The contact to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteContact(Contact contact) {
        boolean isRemoved = allContacts.remove(contact);
        if (isRemoved) {
            FileHandler.saveContacts(allContacts); // Persist the deletion
        }
        return isRemoved;
    }
}