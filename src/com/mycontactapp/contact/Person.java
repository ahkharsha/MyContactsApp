package com.mycontactapp.contact;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Person
 * A contact type for individual people, like friends or family.
 *
 * @author Developer
 * @version 1.0
 */
public class Person extends Contact {
    private String relationship;

    /**
     * Constructs a new Person contact.
     *
     * @param userId       The ID of the owner
     * @param name         The contact's name
     * @param relationship The relationship to the owner
     */
    public Person(String userId, String name, String relationship) {
        super(userId, name);
        this.relationship = relationship;
    }

    /**
     * Reconstructs a Person contact from storage.
     *
     * @param id           The unique contact ID
     * @param userId       The ID of the owner
     * @param name         The contact's name
     * @param phones       List of phone numbers
     * @param emails       List of email addresses
     * @param date         Creation timestamp
     * @param relationship The relationship to the owner
     */
    public Person(String id, String userId, String name, List<String> phones, List<String> emails, LocalDateTime date, String relationship) {
        super(id, userId, name, phones, emails, date);
        this.relationship = relationship;
    }

    /**
     * Gets the relationship to the user.
     *
     * @return The relationship string
     */
    public String getRelationship() { return relationship; }

    /**
     * Sets a new relationship.
     *
     * @param relationship The new relationship
     */
    public void setRelationship(String relationship) { this.relationship = relationship; }
}