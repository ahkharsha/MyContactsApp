package com.mycontactapp.menu;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.tagging.Tag;
import com.mycontactapp.tagging.observer.TagChangeObserver;

/**
 * ConsoleTagObserver
 * Acts as a UI responder when tags are added or removed behind the scenes.
 *
 * @author Developer
 * @version 1.0
 */
public class ConsoleTagObserver implements TagChangeObserver {
    
    @Override
    public void onTagAdded(Contact contact, Tag tag) {
        // Simulating an asynchronous UI/Notification update
        System.out.println("  --> [UI Update] Active Tag '" + tag.toString() + "' was applied to contact: " + contact.getName());
    }

    @Override
    public void onTagRemoved(Contact contact, Tag tag) {
        // Simulating an asynchronous UI/Notification update
        System.out.println("  --> [UI Update] Active Tag '" + tag.toString() + "' was removed from contact: " + contact.getName());
    }
}
