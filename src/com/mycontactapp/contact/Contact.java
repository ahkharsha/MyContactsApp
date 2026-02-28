package com.mycontactapp.contact;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Contact
 * Represents a contact in the address book.
 * Every contact has a name and ID, but specific details come from subclasses.
 *
 * @author Developer
 * @version 1.0
 */
public abstract class Contact {
    private final String contactId;
    private final String userId; // Links the contact to its owner
    private String name;
    private List<String> phoneNumbers;
    private List<String> emailAddresses;
    private final LocalDateTime createdAt;

    /**
     * Constructs a brand new contact.
     * @param userId The ID of the user who owns this contact
     * @param name   The display name of the contact
     */
    public Contact(String userId, String name) {
        this.contactId = UUID.randomUUID().toString();
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        setName(name);
    }

    /**
     * Overloaded constructor for loading existing contacts from a file.
     *
     * @param contactId The existing contact ID
     * @param userId    The ID of the user who owns this contact
     * @param name      The display name of the contact
     * @param phones    The list of phone numbers
     * @param emails    The list of emails
     * @param createdAt The original creation timestamp
     */
    public Contact(String contactId, String userId, String name, List<String> phones, List<String> emails, LocalDateTime createdAt) {
        this.contactId = contactId;
        this.userId = userId;
        this.name = name;
        this.phoneNumbers = phones;
        this.emailAddresses = emails;
        this.createdAt = createdAt;
    }

    /**
     * Gets the unique contact ID.
     *
     * @return The UUID string
     */
    public String getContactId() { return contactId; }

    /**
     * Gets the ID of the user who owns this contact.
     *
     * @return The user ID string
     */
    public String getUserId() { return userId; }

    /**
     * Gets the contact's name.
     *
     * @return The name
     */
    public String getName() { return name; }

    /**
     * Gets the timestamp when the contact was created.
     *
     * @return The LocalDateTime object
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Gets the list of phone numbers.
     *
     * @return A list of phone strings
     */
    public List<String> getPhoneNumbers() { return phoneNumbers; }

    /**
     * Gets the list of email addresses.
     *
     * @return A list of email strings
     */
    public List<String> getEmailAddresses() { return emailAddresses; }

    /**
     * Sets the contact's name.
     *
     * @param name The new name
     * @throws IllegalArgumentException if the name is empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name;
    }

    /**
     * Adds a phone number to the list.
     *
     * @param phone The phone number to add
     */
    public void addPhoneNumber(String phone) { this.phoneNumbers.add(phone); }

    /**
     * Adds an email address to the list.
     *
     * @param email The email address to add
     */
    public void addEmailAddress(String email) { this.emailAddresses.add(email); }
}