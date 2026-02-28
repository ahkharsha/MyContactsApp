package com.mycontactapp.contact;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Organization
 * A contact type for companies or organizations.
 *
 * @author Developer
 * @version 1.0
 */
public class Organization extends Contact {
    private String website;

    /**
     * Constructs a new Organization contact.
     *
     * @param userId  The ID of the owner
     * @param name    The organization's name
     * @param website The website URL
     */
    public Organization(String userId, String name, String website) {
        super(userId, name);
        this.website = website;
    }

    /**
     * Reconstructs an Organization contact from storage.
     *
     * @param id      The unique contact ID
     * @param userId  The ID of the owner
     * @param name    The organization's name
     * @param phones  List of phone numbers
     * @param emails  List of email addresses
     * @param date    Creation timestamp
     * @param website The website URL
     */
    public Organization(String id, String userId, String name, List<String> phones, List<String> emails, LocalDateTime date, String website) {
        super(id, userId, name, phones, emails, date);
        this.website = website;
    }

    /**
     * Gets the website URL.
     *
     * @return The website string
     */
    public String getWebsite() { return website; }

    /**
     * Sets a new website URL.
     *
     * @param website The new website URL
     */
    public void setWebsite(String website) { this.website = website; }
}