package com.mycontactapp.contact.builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ContactBuilder
 * Constructs contact data step-by-step before delegating object creation to the factory.
 *
 * @author Developer
 * @version 1.0
 */
public class ContactBuilder {

    private String contactId;
    private String userId;
    private String name;
    private List<String> phoneNumbers = new ArrayList<>();
    private List<String> emailAddresses = new ArrayList<>();
    private LocalDateTime createdAt = LocalDateTime.now();
    private int viewCount = 0;
    private boolean isActive = true;
    
    // Subclass specific fields
    private String relationship;
    private String website;

    public ContactBuilder setContactId(String contactId) {
        this.contactId = contactId;
        return this;
    }

    public ContactBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public ContactBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ContactBuilder addPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            this.phoneNumbers.add(phoneNumber.trim());
        }
        return this;
    }

    public ContactBuilder setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers != null ? phoneNumbers : new ArrayList<>();
        return this;
    }

    public ContactBuilder addEmailAddress(String emailAddress) {
        if (emailAddress != null && !emailAddress.trim().isEmpty()) {
            this.emailAddresses.add(emailAddress.trim());
        }
        return this;
    }

    public ContactBuilder setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses != null ? emailAddresses : new ArrayList<>();
        return this;
    }

    public ContactBuilder setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ContactBuilder setViewCount(int viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public ContactBuilder setIsActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public ContactBuilder setRelationship(String relationship) {
        this.relationship = relationship;
        return this;
    }

    public ContactBuilder setWebsite(String website) {
        this.website = website;
        return this;
    }

    // Getters for the factory
    public String getContactId() { return contactId; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public List<String> getPhoneNumbers() { return phoneNumbers; }
    public List<String> getEmailAddresses() { return emailAddresses; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getViewCount() { return viewCount; }
    public boolean isActive() { return isActive; }
    public String getRelationship() { return relationship; }
    public String getWebsite() { return website; }
}
