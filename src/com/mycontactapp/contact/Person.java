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

    public Person(String userId, String name, String relationship) {
        super(userId, name);
        this.relationship = relationship;
    }

    public Person(String id, String userId, String name, List<String> phones, List<String> emails, LocalDateTime date, String relationship) {
        super(id, userId, name, phones, emails, date);
        this.relationship = relationship;
    }

    public String getRelationship() { return relationship; }
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