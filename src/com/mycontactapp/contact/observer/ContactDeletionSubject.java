package com.mycontactapp.contact.observer;

import com.mycontactapp.contact.Contact;

/**
 * ContactDeletionSubject
 * Interface for the Subject in the Observer pattern.
 * Manages a list of observers and notifies them on deletion events.
 *
 * @author Developer
 * @version 1.0
 */
public interface ContactDeletionSubject {
    /**
     * Registers a new observer.
     * @param observer The observer to add
     */
    void addObserver(ContactDeletionObserver observer);

    /**
     * Removes an existing observer.
     * @param observer The observer to remove
     */
    void removeObserver(ContactDeletionObserver observer);

    /**
     * Notifies all registered observers that a deletion occurred.
     * @param contact The contact deleted
     * @param isHardDelete true if permanently removed, false if softly deleted
     */
    void notifyObservers(Contact contact, boolean isHardDelete);
}
