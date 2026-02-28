package com.mycontactapp.contact;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Person
 * Represents a single person in your contact list.
 * Includes extra info like how you know them (relationship).
 *
 * @author Developer
 * @version 2.0
 */
public class Person extends Contact {
    private String relationship;

    /**
     * Constructs a new Person contact.
     * @param userId       The ID of the user creating the contact
     * @param name         The name of the person
     * @param relationship The relationship to the user (e.g., "Friend", "Colleague")
     */
    public Person(String userId, String name, String relationship) {
        super(userId, name);
        this.relationship = relationship;
    }

    /**
     * Constructs a Person contact with full details (for loading from storage).
     * @param id           The unique contact ID
     * @param userId       The ID of the user who owns this contact
     * @param name         The name of the person
     * @param phones       List of phone numbers
     * @param emails       List of email addresses
     * @param date         The creation date
     * @param relationship The relationship to the user
     */
    public Person(String id, String userId, String name, List<String> phones, List<String> emails, LocalDateTime date, String relationship) {
        super(id, userId, name, phones, emails, date);
        this.relationship = relationship;
    }

    /**
     * Gets the relationship status.
     * @return The relationship description
     */
    public String getRelationship() { return relationship; }

    /**
     * Updates the relationship status.
     * @param relationship The new relationship description
     */
    public void setRelationship(String relationship) { this.relationship = relationship; }

    /**
     * Shows all the person's details, plus their relationship to you, in a nice format.
     */
    @Override
    public String getFormattedDetails() {
        String relationDisplay = (relationship == null || relationship.trim().isEmpty()) ? "N/A" : relationship;
        return String.format("--- Person Contact ---\n%s\nRelationship: %s\n----------------------", 
                super.getFormattedDetails(), relationDisplay);
    }
}