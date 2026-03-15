package com.mycontactapp.contact.factory;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Organization;
import com.mycontactapp.contact.Person;
import com.mycontactapp.contact.builder.ContactBuilder;

/**
 * ContactFactory
 * Creates the correct concrete contact subtype from pre-assembled builder data.
 *
 * @author Developer
 * @version 1.0
 */
public class ContactFactory {

    /**
     * Creates either a Person or Organization based on the requested contact type.
     * @param type The requested contact type ("Person" or "Organization")
     * @param builder The builder holding the validated contact data
     * @return The constructed concrete Contact subtype
     */
    public Contact createContact(String type, ContactBuilder builder) {
        return switch (type.trim().toLowerCase()) {
            case "person" -> createPerson(builder);
            case "organization" -> createOrganization(builder);
            default -> throw new IllegalArgumentException("Unsupported contact type: " + type);
        };
    }

    /**
     * Creates a Person from builder data.
     * @param builder The builder holding the contact data
     * @return A Person instance
     */
    public Contact createPerson(ContactBuilder builder) {
        String creationMode = builder.getContactId() == null || builder.getContactId().isBlank() ? "new" : "existing";
        return switch (creationMode) {
            case "new" -> {
                Person p = new Person(builder.getUserId(), builder.getName(), builder.getRelationship());
                builder.getPhoneNumbers().forEach(p::addPhoneNumber);
                builder.getEmailAddresses().forEach(p::addEmailAddress);
                yield p;
            }
            case "existing" -> new Person(
                builder.getContactId(), builder.getUserId(), builder.getName(), 
                builder.getPhoneNumbers(), builder.getEmailAddresses(), 
                builder.getCreatedAt(), builder.getRelationship(), 
                builder.getViewCount(), builder.isActive()
            );
            default -> throw new IllegalStateException("Unsupported creation mode: " + creationMode);
        };
    }

    /**
     * Creates an Organization from builder data.
     * @param builder The builder holding the contact data
     * @return An Organization instance
     */
    public Contact createOrganization(ContactBuilder builder) {
        String creationMode = builder.getContactId() == null || builder.getContactId().isBlank() ? "new" : "existing";
        return switch (creationMode) {
            case "new" -> {
                Organization o = new Organization(builder.getUserId(), builder.getName(), builder.getWebsite());
                builder.getPhoneNumbers().forEach(o::addPhoneNumber);
                builder.getEmailAddresses().forEach(o::addEmailAddress);
                yield o;
            }
            case "existing" -> new Organization(
                builder.getContactId(), builder.getUserId(), builder.getName(), 
                builder.getPhoneNumbers(), builder.getEmailAddresses(), 
                builder.getCreatedAt(), builder.getWebsite(), 
                builder.getViewCount(), builder.isActive()
            );
            default -> throw new IllegalStateException("Unsupported creation mode: " + creationMode);
        };
    }
}
