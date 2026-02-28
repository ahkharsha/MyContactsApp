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

    public Organization(String userId, String name, String website) {
        super(userId, name);
        this.website = website;
    }

    public Organization(String id, String userId, String name, List<String> phones, List<String> emails, LocalDateTime date, String website) {
        super(id, userId, name, phones, emails, date);
        this.website = website;
    }

    public String getWebsite() { return website; }
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