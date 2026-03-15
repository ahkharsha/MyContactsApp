package com.mycontactapp.contact.memento;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Organization;
import com.mycontactapp.contact.Person;

/**
 * ContactMemento
 * Stores a deep-copied snapshot of a Contact's state at a specific point in time.
 * Used by the Command pattern to implement Undo/Redo functionality.
 *
 * @author Developer
 * @version 1.0
 */
public class ContactMemento {
    private final Contact state;

    /**
     * Initializes the memento with a deep copy of the provided contact.
     * @param contact The contact whose state needs preserving
     */
    public ContactMemento(Contact contact) {
        if (contact instanceof Person) {
            this.state = new Person((Person) contact);
        } else if (contact instanceof Organization) {
            this.state = new Organization((Organization) contact);
        } else {
            throw new IllegalArgumentException("Unsupported Contact type for Memento");
        }
    }

    /**
     * Retrieves the preserved snapshot of the contact.
     * @return The preserved Contact state
     */
    public Contact getState() {
        return state;
    }
}
