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
 * @version 6.0
 */
public class ContactService {
    
    private final List<Contact> allContacts;

    /**
     * Constructs the ContactService and loads existing contacts from file.
     */
    public ContactService() {
        this.allContacts = FileHandler.loadContacts();
    }

    /**
     * Creates a new Person contact for a user.
     * @param owner        The user creating the contact
     * @param name         The name of the new contact
     * @param phone        The phone number
     * @param email        The email address
     * @param relationship The relationship to the user
     * @return The newly created Person object
     * @throws ContactAppException if the user has reached their contact limit
     */
    public Person createPersonContact(User owner, String name, String phone, String email, String relationship) throws ContactAppException {
        enforceContactLimit(owner);
        Person person = new Person(owner.getUserId(), name, relationship);
        if (!phone.isEmpty()) person.addPhoneNumber(phone);
        if (!email.isEmpty()) person.addEmailAddress(email);
        
        allContacts.add(person);
        FileHandler.saveContacts(allContacts); 
        return person;
    }

    /**
     * Creates a new Organization contact for a user.
     * @param owner   The user creating the contact
     * @param name    The name of the organization
     * @param phone   The phone number
     * @param email   The email address
     * @param website The website URL
     * @return The newly created Organization object
     * @throws ContactAppException if the user has reached their contact limit
     */
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
     * @param owner The user whose contacts should be retrieved
     * @return A list of contacts owned by the user
     */
    public List<Contact> getUserContacts(User owner) {
        return allContacts.stream()
                .filter(contact -> contact.getUserId().equals(owner.getUserId()))
                .collect(Collectors.toList());
    }

    /**
     * Updates the name of an existing contact.
     * @param contact The contact to update
     * @param newName The new name to assign
     * @throws ContactAppException if the new name is invalid
     */
    public void updateContactName(Contact contact, String newName) throws ContactAppException {
        if (newName == null || newName.trim().isEmpty()) throw new ContactAppException("Contact name cannot be empty.");
        contact.setName(newName);
        FileHandler.saveContacts(allContacts);
    }

    /**
     * Adds a new phone number to a contact.
     * @param contact  The contact to update
     * @param newPhone The phone number to add
     * @throws ContactAppException if the phone number is invalid
     */
    public void addPhoneToContact(Contact contact, String newPhone) throws ContactAppException {
        if (newPhone == null || newPhone.trim().isEmpty()) throw new ContactAppException("Phone number cannot be empty.");
        contact.addPhoneNumber(newPhone);
        FileHandler.saveContacts(allContacts);
    }

    /**
     * Adds a new email address to a contact.
     * @param contact  The contact to update
     * @param newEmail The email address to add
     * @throws ContactAppException if the email address is invalid
     */
    public void addEmailToContact(Contact contact, String newEmail) throws ContactAppException {
        if (newEmail == null || newEmail.trim().isEmpty()) throw new ContactAppException("Email address cannot be empty.");
        contact.addEmailAddress(newEmail);
        FileHandler.saveContacts(allContacts);
    }

    /**
     * Removes a contact from the system permanently.
     * @param contact The contact to delete
     * @return true if the contact was found and removed, false otherwise
     */
    public boolean deleteContact(Contact contact) {
        boolean isRemoved = allContacts.remove(contact);
        if (isRemoved) FileHandler.saveContacts(allContacts);
        return isRemoved;
    }

    /**
     * Deletes multiple contacts at once.
     * @param contactsToDelete The list of contacts to remove
     * @return The number of contacts successfully deleted
     */
    public int bulkDeleteContacts(List<Contact> contactsToDelete) {
        if (contactsToDelete == null || contactsToDelete.isEmpty()) return 0;
        int count = 0;
        for (Contact c : contactsToDelete) {
            if (allContacts.remove(c)) count++;
        }
        if (count > 0) FileHandler.saveContacts(allContacts);
        return count;
    }

    /**
     * Retrieves all unique tags used across a user's contact list.
     * @param owner The user whose tags should be retrieved
     * @return A set of unique tags
     */
    public java.util.Set<com.mycontactapp.tagging.Tag> getAllUserTags(User owner) {
        java.util.Set<com.mycontactapp.tagging.Tag> uniqueTags = new java.util.HashSet<>();
        for (Contact contact : getUserContacts(owner)) {
            uniqueTags.addAll(contact.getTags());
        }
        return uniqueTags;
    }

    /**
     * Helper to check if a user can create more contacts.
     * @param owner The user to check
     * @throws ContactAppException if the limit is reached
     */
    private void enforceContactLimit(User owner) throws ContactAppException {
        long currentCount = getUserContacts(owner).size();
        if (currentCount >= owner.getContactLimit()) {
            throw new ContactAppException("Contact limit reached for your account type. Upgrade to Premium!");
        }
    }
}