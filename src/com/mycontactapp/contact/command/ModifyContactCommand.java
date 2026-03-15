package com.mycontactapp.contact.command;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.contact.memento.ContactMemento;

import java.util.function.Consumer;

/**
 * ModifyContactCommand
 * A generic command implementation that captures state via Mementos 
 * before and after applying a functional mutation.
 */
public class ModifyContactCommand implements EditContactCommand {
    private final Contact contact;
    private final ContactService contactService;
    private final Consumer<Contact> mutator;
    
    private ContactMemento preEditState;
    private ContactMemento postEditState;

    /**
     * @param contact The contact being edited
     * @param contactService Service to invoke save operations
     * @param mutator A Consumer that actually applies the change to the contact
     */
    public ModifyContactCommand(Contact contact, ContactService contactService, Consumer<Contact> mutator) {
        this.contact = contact;
        this.contactService = contactService;
        this.mutator = mutator;
    }

    @Override
    public void execute() {
        // Save state before modifying
        preEditState = contact.saveState();
        
        // Execute the modification
        mutator.accept(contact);
        
        // Save state after modifying and persist
        postEditState = contact.saveState();
        contactService.saveAllContacts();
    }

    @Override
    public void undo() {
        if (preEditState != null) {
            contact.restoreState(preEditState);
            contactService.saveAllContacts();
        }
    }

    @Override
    public void redo() {
        if (postEditState != null) {
            contact.restoreState(postEditState);
            contactService.saveAllContacts();
        }
    }
}
