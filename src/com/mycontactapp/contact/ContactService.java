package com.mycontactapp.contact;

import com.mycontactapp.contact.builder.ContactBuilder;
import com.mycontactapp.contact.factory.ContactFactory;
import com.mycontactapp.contact.observer.ContactDeletionObserver;
import com.mycontactapp.contact.observer.ContactDeletionSubject;
import com.mycontactapp.tagging.observer.TagChangeObserver;
import com.mycontactapp.tagging.observer.TagChangeSubject;
import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.tagging.Tag;
import com.mycontactapp.user.model.User;
import com.mycontactapp.util.FileHandler;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mycontactapp.tagging.TagFactory;

/**
 * ContactService
 * Manages core contact operations such as creation and data retrieval.
 *
 * @author Developer
 * @version 8.0
 */
public class ContactService implements ContactDeletionSubject, TagChangeSubject {
    
    private final List<Contact> allContacts;
    private final ContactFactory contactFactory;
    private final List<ContactDeletionObserver> observers;
    private final List<TagChangeObserver> tagObservers;

    /**
     * Constructs a new ContactService and loads existing contacts from file.
     */
    public ContactService() {
        this.allContacts = FileHandler.loadContacts();
        this.contactFactory = new ContactFactory();
        this.observers = new ArrayList<>();
        this.tagObservers = new ArrayList<>();
    }

    /**
     * Creates and saves a new Person contact.
     * @param owner The user creating the contact
     * @param name The name of the person
     * @param phone Optional phone number
     * @param email Optional email address
     * @param relationship Relationship to the user
     * @return The created Person object
     * @throws ContactAppException if creation fails or limit reached
     */
    public Person createPersonContact(User owner, String name, String phone, String email, String relationship) throws ContactAppException {
        enforceContactLimit(owner);
        ContactBuilder builder = new ContactBuilder()
            .setUserId(owner.getUserId())
            .setName(name)
            .setRelationship(relationship)
            .addPhoneNumber(phone)
            .addEmailAddress(email);

        Person person = (Person) contactFactory.createContact("Person", builder);
        
        allContacts.add(person);
        FileHandler.saveContacts(allContacts); 
        return person;
    }

    /**
     * Creates and saves a new Organization contact.
     * @param owner The user creating the contact
     * @param name The organization name
     * @param phone Optional phone number
     * @param email Optional email address
     * @param website The organization's website
     * @return The created Organization object
     * @throws ContactAppException if creation fails or limit reached
     */
    public Organization createOrganizationContact(User owner, String name, String phone, String email, String website) throws ContactAppException {
        enforceContactLimit(owner);
        ContactBuilder builder = new ContactBuilder()
            .setUserId(owner.getUserId())
            .setName(name)
            .setWebsite(website)
            .addPhoneNumber(phone)
            .addEmailAddress(email);
            
        Organization org = (Organization) contactFactory.createContact("Organization", builder);
        
        allContacts.add(org);
        FileHandler.saveContacts(allContacts); 
        return org;
    }

    /**
     * Retrieves all contacts belonging to a specific user.
     * @param owner The user whose contacts to retrieve
     * @return A list of the user's contacts
     */
    public List<Contact> getUserContacts(User owner) {
        return allContacts.stream()
                .filter(contact -> contact.getUserId().equals(owner.getUserId()) && contact.isActive())
                .collect(Collectors.toList());
    }

    /**
     * Updates a contact's name and saves the change.
     * @param contact The contact to update
     * @param newName The new name
     * @throws ContactAppException if the name is invalid
     */
    public void updateContactName(Contact contact, String newName) throws ContactAppException {
        if (newName == null || newName.trim().isEmpty()) throw new ContactAppException("Contact name cannot be empty.");
        contact.setName(newName);
        saveAllContacts();
    }

    /**
     * Persists all currently loaded contacts to storage.
     * Useful when changes are made via generic Commands.
     */
    public void saveAllContacts() {
        FileHandler.saveContacts(allContacts);
    }

    /**
     * Adds a phone number to a contact and saves.
     * @param contact The contact to update
     * @param newPhone The phone number to add
     * @throws ContactAppException if the number is invalid
     */
    public void addPhoneToContact(Contact contact, String newPhone) throws ContactAppException {
        if (newPhone == null || newPhone.trim().isEmpty()) throw new ContactAppException("Phone number cannot be empty.");
        contact.addPhoneNumber(newPhone);
        saveAllContacts();
    }

    /**
     * Adds an email address to a contact and saves.
     * @param contact The contact to update
     * @param newEmail The email address to add
     * @throws ContactAppException if the email is invalid
     */
    public void addEmailToContact(Contact contact, String newEmail) throws ContactAppException {
        if (newEmail == null || newEmail.trim().isEmpty()) throw new ContactAppException("Email address cannot be empty.");
        contact.addEmailAddress(newEmail);
        saveAllContacts();
    }

    /**
     * Adds a tag to a contact and saves.
     * @param contact The contact to update
     * @param tagName The name of the tag to add
     * @throws ContactAppException if the tag name is invalid
     */
    public void addTagToContact(Contact contact, String tagName) throws ContactAppException {
        try {
            Tag newTag = TagFactory.getTag(tagName);
            contact.addTag(newTag);
            notifyTagAdded(contact, newTag);
            saveAllContacts();
        } catch (IllegalArgumentException e) {
            throw new ContactAppException(e.getMessage());
        }
    }

    /**
     * Removes a tag from a contact and saves.
     * @param contact The contact to update
     * @param tagName The name of the tag to remove
     * @throws ContactAppException if the tag name is invalid
     */
    public void removeTagFromContact(Contact contact, String tagName) throws ContactAppException {
        try {
            Tag targetTag = TagFactory.getTag(tagName);
            contact.removeTag(targetTag);
            notifyTagRemoved(contact, targetTag);
            saveAllContacts();
        } catch (IllegalArgumentException e) {
            throw new ContactAppException(e.getMessage());
        }
    }

    /**
     * Deletes a single contact, firing notifications to observers.
     * @param contact The contact to delete
     * @param isHardDelete True completely removes the contact, False just marks as inactive (Soft Delete).
     * @return true if successful
     */
    public boolean deleteContact(Contact contact, boolean isHardDelete) {
        if (isHardDelete) {
            allContacts.remove(contact);
        } else {
            contact.setActive(false);
        }
        
        notifyObservers(contact, isHardDelete);
        saveAllContacts();
        return true;
    }

    /**
     * Overloaded method for backwards compatibility with soft deletes by default.
     * @param contact The contact to soft-delete
     * @return true if successful
     */
    public boolean deleteContact(Contact contact) {
        return deleteContact(contact, false);
    }

    /**
     * Deletes multiple contacts at once (Soft Delete).
     * @param contactsToDelete The list of contacts to remove
     * @return The number of contacts successfully deleted
     */
    public int bulkDeleteContacts(List<Contact> contactsToDelete) {
        if (contactsToDelete == null || contactsToDelete.isEmpty()) return 0;
        int count = 0;
        for (Contact c : contactsToDelete) {
            c.setActive(false);
            count++;
        }
        if (count > 0) saveAllContacts();
        return count;
    }

    /**
     * Increments the view count for a contact.
     * @param contact The contact viewed
     */
    public void incrementContactViewCount(Contact contact) {
        contact.incrementViewCount();
        saveAllContacts();
    }

    /**
     * Retrieves all unique tags used across a user's contacts.
     * @param owner The user
     * @return A set of unique tags
     */
    public Set<Tag> getAllUserTags(User owner) {
        Set<Tag> uniqueTags = new HashSet<>();
        for (Contact contact : getUserContacts(owner)) {
            uniqueTags.addAll(contact.getTags());
        }
        return uniqueTags;
    }

    /**
     * Checks if the user has reached their contact storage limit.
     * @param owner The user to check
     * @throws ContactAppException if the limit is reached
     */
    private void enforceContactLimit(User owner) throws ContactAppException {
        long currentCount = getUserContacts(owner).size();
        if (currentCount >= owner.getContactLimit()) {
            throw new ContactAppException("Contact limit reached for your account type. Upgrade to Premium!");
        }
    }

    // --- Observer Pattern Methods --- \\

    @Override
    public void addObserver(ContactDeletionObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(ContactDeletionObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Contact contact, boolean isHardDelete) {
        for (ContactDeletionObserver observer : observers) {
            observer.onContactDeleted(contact, isHardDelete);
        }
    }

    // --- Tag Observer Pattern Methods --- \\

    @Override
    public void addTagObserver(TagChangeObserver observer) {
        if (!tagObservers.contains(observer)) {
            tagObservers.add(observer);
        }
    }

    @Override
    public void removeTagObserver(TagChangeObserver observer) {
        tagObservers.remove(observer);
    }

    @Override
    public void notifyTagAdded(Contact contact, Tag tag) {
        for (TagChangeObserver observer : tagObservers) {
            observer.onTagAdded(contact, tag);
        }
    }

    @Override
    public void notifyTagRemoved(Contact contact, Tag tag) {
        for (TagChangeObserver observer : tagObservers) {
            observer.onTagRemoved(contact, tag);
        }
    }
}