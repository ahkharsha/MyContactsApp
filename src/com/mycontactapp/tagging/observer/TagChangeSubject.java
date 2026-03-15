package com.mycontactapp.tagging.observer;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.tagging.Tag;

/**
 * TagChangeSubject
 * Interface for the Subject in the Observer Pattern.
 * Implemented by components (like ContactService) that manage tags
 * and notify Observers of changes.
 *
 * @author Developer
 * @version 1.0
 */
public interface TagChangeSubject {
    /**
     * Registers an observer to receive tag change notifications.
     * @param observer The observer to add
     */
    void addTagObserver(TagChangeObserver observer);

    /**
     * Deregisters an observer.
     * @param observer The observer to remove
     */
    void removeTagObserver(TagChangeObserver observer);

    /**
     * Notifies all registered observers that a tag was added.
     * @param contact The modified contact
     * @param tag The newly added tag
     */
    void notifyTagAdded(Contact contact, Tag tag);

    /**
     * Notifies all registered observers that a tag was removed.
     * @param contact The modified contact
     * @param tag The removed tag
     */
    void notifyTagRemoved(Contact contact, Tag tag);
}
