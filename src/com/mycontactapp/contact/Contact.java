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
 * @version 2.0
 */
public abstract class Contact {
    private final String contactId;
    private final String userId; 
    private String name;
    private List<String> phoneNumbers;
    private List<String> emailAddresses;
    private final LocalDateTime createdAt;

    public Contact(String userId, String name) {
        this.contactId = UUID.randomUUID().toString();
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.phoneNumbers = new ArrayList<>();
        this.emailAddresses = new ArrayList<>();
        setName(name);
    }

    public Contact(String contactId, String userId, String name, List<String> phones, List<String> emails, LocalDateTime createdAt) {
        this.contactId = contactId;
        this.userId = userId;
        this.name = name;
        this.phoneNumbers = phones;
        this.emailAddresses = emails;
        this.createdAt = createdAt;
    }

    public String getContactId() { return contactId; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<String> getPhoneNumbers() { return phoneNumbers; }
    public List<String> getEmailAddresses() { return emailAddresses; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name;
    }

    public void addPhoneNumber(String phone) { this.phoneNumbers.add(phone); }
    public void addEmailAddress(String email) { this.emailAddresses.add(email); }

    /**
     * Creates a formatted string with all the contact's details.
     * Nice for displaying the full info to the user.
     *
     * @return Formatted string containing contact details
     */
    public String getFormattedDetails() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        // Using Optional to safely handle empty collections for display
        String phones = Optional.ofNullable(phoneNumbers).filter(list -> !list.isEmpty())
                .map(list -> String.join(", ", list))
                .orElse("No phone numbers provided");
                
        String emails = Optional.ofNullable(emailAddresses).filter(list -> !list.isEmpty())
                .map(list -> String.join(", ", list))
                .orElse("No email addresses provided");

        return String.format(
            "Name: %s\nPhones: %s\nEmails: %s\nAdded On: %s",
            name, phones, emails, createdAt.format(dtf)
        );
    }
}