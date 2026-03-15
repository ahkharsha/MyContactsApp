package com.mycontactapp.contact;

import com.mycontactapp.tagging.Tag;
import java.util.List;

/**
 * ContactComponent
 * Base interface for the Composite Pattern, allowing individual contacts 
 * and groups of contacts to be treated uniformly.
 *
 * @author Developer
 * @version 1.0
 */
public interface ContactComponent {
    /**
     * Gets the name of the component (Contact name or Group name).
     */
    String getName();

    /**
     * Adds a tag to the component (either single contact or all contacts in a group).
     */
    void addTag(Tag tag);

    /**
     * Removes a tag from the component.
     */
    void removeTag(Tag tag);

    /**
     * Changes the active status (useful for soft delete).
     */
    void setActive(boolean isActive);

    /**
     * Gets formatted details (either single contact details or a summary of the group).
     */
    String getFormattedDetails();

    /**
     * Flattens the hierarchy into a simple list of Contacts.
     */
    List<Contact> getAsContactList();
}
