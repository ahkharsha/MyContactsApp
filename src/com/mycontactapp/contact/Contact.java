package com.mycontactapp.contact;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Contact
 * Abstract base class representing a generic contact.
 *
 * @author Developer
 * @version 3.0
 */
public abstract class Contact {
    private final String contactId;
    private final String userId; 
    private String name;
    private List<String> phoneNumbers;
    private List<String> emailAddresses;
    private final LocalDateTime createdAt;
    private java.util.Set<com.mycontactapp.tagging.Tag> tags;

    /**
     * Constructs a new Contact for a specific user.
     * @param userId The ID of the user who owns this contact
     * @param name   The name of the contact
     */
    public Contact(String userId, String name) {
        this.contactId = UUID.randomUUID().toString();
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        this.tags = new java.util.HashSet<>();
        setName(name);
    }

    /**
     * Constructs a Contact with full details (useful for loading from storage).
     * @param contactId   The unique ID of the contact
     * @param userId      The ID of the user who owns this contact
     * @param name        The name of the contact
     * @param phones      List of phone numbers
     * @param emails      List of email addresses
     * @param createdAt   The date and time the contact was created
     */
    public Contact(String contactId, String userId, String name, List<String> phones, List<String> emails, LocalDateTime createdAt) {
        this.contactId = contactId;
        this.userId = userId;
        this.name = name;
        this.phoneNumbers = phones;
        this.emailAddresses = emails;
        this.createdAt = createdAt;
        this.tags = new java.util.HashSet<>();
    }

    /**
     * Gets the unique ID of the contact.
     * @return The contact ID
     */
    public String getContactId() { return contactId; }

    /**
     * Gets the ID of the user who owns this contact.
     * @return The user ID
     */
    public String getUserId() { return userId; }

    /**
     * Gets the name of the contact.
     * @return The contact name
     */
    public String getName() { return name; }

    /**
     * Gets the creation date and time of the contact.
     * @return The creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Gets the list of phone numbers associated with the contact.
     * @return List of phone numbers
     */
    public List<String> getPhoneNumbers() { return phoneNumbers; }
    /**
     * Gets the list of email addresses associated with the contact.
     * @return List of email addresses
     */
    public List<String> getEmailAddresses() { return emailAddresses; }

    /**
     * Gets the set of tags associated with the contact.
     * @return Set of tags
     */
    public java.util.Set<com.mycontactapp.tagging.Tag> getTags() { return tags; }

    /**
     * Updates the name of the contact.
     * @param name The new name
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name;
    }

    /**
     * Adds a phone number to the contact.
     * @param phone The phone number to add
     */
    public void addPhoneNumber(String phone) { this.phoneNumbers.add(phone); }

    /**
     * Adds an email address to the contact.
     * @param email The email address to add
     */
    public void addEmailAddress(String email) { this.emailAddresses.add(email); }

    /**
     * Adds a tag to the contact.
     * @param tag The tag object to add
     */
    public void addTag(com.mycontactapp.tagging.Tag tag) { this.tags.add(tag); }

    /**
     * Removes a tag from the contact.
     * @param tag The tag object to remove
     */
    public void removeTag(com.mycontactapp.tagging.Tag tag) { this.tags.remove(tag); }

    /**
     * Returns a formatted string containing the contact's details.
     * @return A string with formatted contact information
     */
    public String getFormattedDetails() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        String phones = Optional.ofNullable(phoneNumbers).filter(list -> !list.isEmpty())
                .map(list -> String.join(", ", list))
                .orElse("No phone numbers provided");
                
        String emails = Optional.ofNullable(emailAddresses).filter(list -> !list.isEmpty())
                .map(list -> String.join(", ", list))
                .orElse("No email addresses provided");

        String tagDisplay = tags.isEmpty() ? "No Tags" : 
                tags.stream().map(com.mycontactapp.tagging.Tag::toString).reduce((t1, t2) -> t1 + " " + t2).get();

        return String.format(
            "Name: %s\nPhones: %s\nEmails: %s\nTags: %s\nAdded On: %s",
            name, phones, emails, tagDisplay, createdAt.format(dtf)
        );
    }
}