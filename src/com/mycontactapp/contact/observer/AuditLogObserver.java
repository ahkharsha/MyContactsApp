package com.mycontactapp.contact.observer;

import com.mycontactapp.contact.Contact;

import java.time.LocalDateTime;

/**
 * AuditLogObserver
 * A concrete observer that logs deletion events for auditing purposes.
 *
 * @author Developer
 * @version 1.0
 */
public class AuditLogObserver implements ContactDeletionObserver {

    @Override
    public void onContactDeleted(Contact contact, boolean isHardDelete) {
        String deletionType = isHardDelete ? "HARD DELETED (Permanently Removed)" : "SOFT DELETED (Archived)";
        System.out.println("\n[SYSTEM AUDIT LOG - " + LocalDateTime.now() + "]");
        System.out.println("ACTION: " + deletionType);
        System.out.println("USER ID: " + contact.getUserId());
        System.out.println("CONTACT ID: " + contact.getContactId());
        System.out.println("CONTACT NAME: " + contact.getName());
        System.out.println("--------------------------------------------------\n");
    }
}
