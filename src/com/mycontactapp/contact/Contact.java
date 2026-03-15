package com.mycontactapp.contact;

import com.mycontactapp.contact.decorator.ContactDisplay;
import com.mycontactapp.contact.memento.ContactMemento;
import com.mycontactapp.tagging.Tag;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contact
 * Abstract base class representing a generic contact.
 *
 * @author Developer
 * @version 4.0
 */
public abstract class Contact implements ContactDisplay, ContactComponent {
    private final String contactId;
    private final String userId; 
    private String name;
    private List<PhoneNumber> phoneNumbers;
    private List<Email> emailAddresses;
    private final LocalDateTime createdAt;
    private Set<Tag> tags;
    private int viewCount;
    private boolean isActive;

    /**
     * Constructs a new Contact for a specific user.
     * @param userId The ID of the user who owns this contact
     * @param name The name of the contact
     */
    public Contact(String userId, String name) {
        this.contactId = UUID.randomUUID().toString();
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        this.tags = new HashSet<>();
        this.viewCount = 0;
        this.isActive = true;
        setName(name);
    }

    /**
     * Constructs a Contact with full details (useful for loading from storage).
     * @param contactId The unique ID of the contact
     * @param userId The ID of the user who owns this contact
     * @param name The name of the contact
     * @param phones List of phone numbers
     * @param emails List of email addresses
     * @param createdAt The date and time the contact was created
     * @param viewCount The number of times the contact has been viewed
     * @param isActive Whether the contact is active (not soft-deleted)
     */
    public Contact(String contactId, String userId, String name, List<String> phones, List<String> emails, LocalDateTime createdAt, int viewCount, boolean isActive) {
        this.contactId = contactId;
        this.userId = userId;
        this.name = name;
        this.phoneNumbers = phones != null ? phones.stream().map(PhoneNumber::new).collect(Collectors.toList()) : new ArrayList<>();
        this.emailAddresses = emails != null ? emails.stream().map(Email::new).collect(Collectors.toList()) : new ArrayList<>();
        this.createdAt = createdAt;
        this.tags = new HashSet<>();
        this.viewCount = viewCount;
        this.isActive = isActive;
    }

    /**
     * Copy constructor for creating deep clones (used by Memento).
     * @param other The contact to copy
     */
    public Contact(Contact other) {
        this.contactId = other.contactId;
        this.userId = other.userId;
        this.name = other.name;
        // Deep copy lists/sets to ensure mutations don't leak across snapshots
        this.phoneNumbers = other.phoneNumbers.stream().map(p -> new PhoneNumber(p.getNumber())).collect(Collectors.toList());
        this.emailAddresses = other.emailAddresses.stream().map(e -> new Email(e.getAddress())).collect(Collectors.toList());
        this.tags = new HashSet<>();
        for (Tag t : other.tags) {
            this.tags.add(new Tag(t.getName()));
        }
        this.createdAt = other.createdAt;
        this.viewCount = other.viewCount;
        this.isActive = other.isActive;
    }

    /**
     * Creates a Memento capturing the current state of this contact.
     * @return A ContactMemento containing a snapshot
     */
    public ContactMemento saveState() {
        return new ContactMemento(this);
    }

    /**
     * Restores the state of this contact from a Memento.
     * @param memento The previously saved snapshot
     */
    public void restoreState(ContactMemento memento) {
        Contact snapshot = memento.getState();
        this.name = snapshot.getName();
        this.phoneNumbers = snapshot.getPhoneNumbers().stream().map(PhoneNumber::new).collect(Collectors.toList());
        this.emailAddresses = snapshot.getEmailAddresses().stream().map(Email::new).collect(Collectors.toList());
        this.tags = new HashSet<>();
        for (Tag t : snapshot.getTags()) {
            this.tags.add(new Tag(t.getName()));
        }
        this.viewCount = snapshot.getViewCount();
        this.isActive = snapshot.isActive();
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
     * @return List of phone numbers as Strings
     */
    public List<String> getPhoneNumbers() { 
        return phoneNumbers.stream().map(PhoneNumber::getNumber).collect(Collectors.toList()); 
    }
    
    /**
     * Gets the list of email addresses associated with the contact.
     * @return List of email addresses as Strings
     */
    public List<String> getEmailAddresses() { 
        return emailAddresses.stream().map(Email::getAddress).collect(Collectors.toList()); 
    }

    /**
     * Gets the set of tags associated with the contact.
     * @return Set of tags
     */
    public Set<Tag> getTags() { return tags; }

    /**
     * Gets the view count of the contact.
     * @return The view count
     */
    public int getViewCount() { return viewCount; }

    /**
     * Increments the view count by 1.
     */
    public void incrementViewCount() { this.viewCount++; }

    /**
     * Checks if the contact is active (not deleted).
     * @return true if active, false otherwise
     */
    public boolean isActive() { return isActive; }

    /**
     * Sets the active status of the contact.
     * @param isActive true to activate, false to soft-delete
     */
    public void setActive(boolean isActive) { this.isActive = isActive; }

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
    public void addPhoneNumber(String phone) { this.phoneNumbers.add(new PhoneNumber(phone)); }

    /**
     * Adds an email address to the contact.
     * @param email The email address to add
     */
    public void addEmailAddress(String email) { this.emailAddresses.add(new Email(email)); }

    /**
     * Adds a tag to the contact.
     * @param tag The tag object to add
     */
    public void addTag(Tag tag) { this.tags.add(tag); }

    /**
     * Removes a tag from the contact.
     * @param tag The tag object to remove
     */
    public void removeTag(Tag tag) { this.tags.remove(tag); }

    /**
     * Returns a formatted string containing the contact's details.
     * @return A string with formatted contact information
     */
    @Override
    public String getFormattedDetails() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        String phones = Optional.ofNullable(phoneNumbers).filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(PhoneNumber::getNumber).collect(Collectors.joining(", ")))
                .orElse("No phone numbers provided");
                
        String emails = Optional.ofNullable(emailAddresses).filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(Email::getAddress).collect(Collectors.joining(", ")))
                .orElse("No email addresses provided");

        String tagDisplay = tags.isEmpty() ? "No Tags" : 
                tags.stream().map(Tag::toString).reduce((t1, t2) -> t1 + " " + t2).get();

        return String.format(
            "Name: %s\nPhones: %s\nEmails: %s\nTags: %s\nAdded On: %s",
            name, phones, emails, tagDisplay, createdAt.format(dtf)
        );
    }

    /**
     * Returns this single contact wrapped in a list.
     * @return List containing only this contact
     */
    @Override
    public List<Contact> getAsContactList() {
        List<Contact> singleList = new ArrayList<>();
        singleList.add(this);
        return singleList;
    }
}