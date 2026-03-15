package com.mycontactapp.contact;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Organization
 * Represents a company or organization in your contact list.
 * Includes their website address.
 *
 * @author Developer
 * @version 2.0
 */
public class Organization extends Contact {
    private String website;

    /**
     * Constructs a new Organization contact.
     * @param userId The ID of the user creating the contact
     * @param name The name of the organization
     * @param website The website URL of the organization
     */
    public Organization(String userId, String name, String website) {
        super(userId, name);
        this.website = website;
    }

    /**
     * Constructs an Organization contact with full details (for loading from storage).
     * @param id The unique contact ID
     * @param userId The ID of the user who owns this contact
     * @param name The name of the organization
     * @param phones List of phone numbers
     * @param emails List of email addresses
     * @param date The creation date
     * @param website The website URL
     * @param viewCount The view count
     * @param isActive Whether the contact is active
     */
    public Organization(String id, String userId, String name, List<String> phones, List<String> emails, LocalDateTime date, String website, int viewCount, boolean isActive) {
        super(id, userId, name, phones, emails, date, viewCount, isActive);
        this.website = website;
    }

    /**
     * Copy constructor for creating deep clones (used by Memento).
     * @param other The Organization contact to copy
     */
    public Organization(Organization other) {
        super(other);
        this.website = other.website;
    }

    /**
     * Gets the website URL.
     * @return The website URL
     */
    public String getWebsite() { return website; }

    /**
     * Updates the website URL.
     * @param website The new website URL
     */
    public void setWebsite(String website) { this.website = website; }

    /**
     * Shows all the organization's details, plus their website, in a nice format.
     */
    @Override
    public String getFormattedDetails() {
        String siteDisplay = (website == null || website.trim().isEmpty()) ? "N/A" : website;
        return String.format("--- Organization Contact ---\n%s\nWebsite: %s\n----------------------------", 
                super.getFormattedDetails(), siteDisplay);
    }
}