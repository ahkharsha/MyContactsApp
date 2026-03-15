package com.mycontactapp.contact.observer;

import com.mycontactapp.contact.Contact;

/**
 * ContactDeletionObserver
 * Interface for components that wish to be notified of contact deletion events.
 *
 * @author Developer
 * @version 1.0
 */
public interface ContactDeletionObserver {
    /**
     * Called when a contact is deleted.
     * @param contact The contact that was deleted
     * @param isHardDelete true if permanently removed, false if softly marked inactive
     */
    void onContactDeleted(Contact contact, boolean isHardDelete);
}
