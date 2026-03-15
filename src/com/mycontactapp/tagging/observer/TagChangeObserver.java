package com.mycontactapp.tagging.observer;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.tagging.Tag;

/**
 * TagChangeObserver
 * Interface for the Observer in the Observer Pattern.
 * Components implementing this interface want to be notified
 * whenever a tag is added to or removed from a Contact.
 *
 * @author Developer
 * @version 1.0
 */
public interface TagChangeObserver {
    /**
     * Called when a tag is added to a contact.
     * @param contact The contact receiving the tag
     * @param tag The tag that was added
     */
    void onTagAdded(Contact contact, Tag tag);

    /**
     * Called when a tag is removed from a contact.
     * @param contact The contact losing the tag
     * @param tag The tag that was removed
     */
    void onTagRemoved(Contact contact, Tag tag);
}
