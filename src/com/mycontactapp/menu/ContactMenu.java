package com.mycontactapp.menu;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.contact.decorator.ContactDisplay;
import com.mycontactapp.contact.decorator.MaskedEmailDecorator;
import com.mycontactapp.contact.decorator.UpperCaseDecorator;
import com.mycontactapp.contact.command.ContactEditInvoker;
import com.mycontactapp.contact.command.ModifyContactCommand;
import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * ContactMenu
 * Manages contact management flows (create, view, edit).
 * @author Developer
 * @version 1.0
 */
public class ContactMenu {

    /**
     * Handles input for creating a new contact (Person or Organization).
     * @param contactService The service to create contacts
     * @param scanner The input scanner
     * @param loggedInUser The user creating the contact
     */
    public static void createContactFlow(ContactService contactService, Scanner scanner, User loggedInUser) {
        System.out.println("\n-- Add Contact --");
        System.out.println("1. Add Person");
        System.out.println("2. Add Organization");
        System.out.print("Choose contact type: ");
        String type = scanner.nextLine();
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Phone (optional): "); String phone = scanner.nextLine();
        System.out.print("Email (optional): "); String email = scanner.nextLine();
        try {
            switch (type) {
                case "1" -> {
                    System.out.print("Relationship (e.g. Friend, Brother): ");
                    contactService.createPersonContact(loggedInUser, name, phone, email, scanner.nextLine());
                    System.out.println("Person Contact added successfully!");
                }
                case "2" -> {
                    System.out.print("Website: ");
                    contactService.createOrganizationContact(loggedInUser, name, phone, email, scanner.nextLine());
                    System.out.println("Organization Contact added successfully!");
                }
                default -> System.out.println("Invalid type selected.");
            }
        } catch (ContactAppException e) { System.err.println("Failed to add contact: " + e.getMessage()); }
    }

    /**
     * Display all contacts for the logged-in user.
     * @param contactService The service to retrieve contacts
     * @param loggedInUser The logged-in user
     * @param scanner The input scanner
     */
    public static void viewContactsFlow(ContactService contactService, User loggedInUser, Scanner scanner) {
        System.out.println("\n-- My Contacts --");
        List<Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { 
            System.out.println("You have no contacts saved yet."); 
            return;
        } 
        
        System.out.println("Display format options:");
        System.out.println("1. Standard Format");
        System.out.println("2. ALL UPPERCASE");
        System.out.println("3. Privacy Mode (Mask Emails)");
        System.out.println("4. Ultra Privacy Mode (UPPERCASE + Mask Emails)");
        System.out.print("Choose display format: ");
        String formatChoice = scanner.nextLine();

        for (int i = 0; i < userContacts.size(); i++) { 
            System.out.println("\n[" + (i + 1) + "]"); 
            
            ContactDisplay displayContact = userContacts.get(i);
            
            // Apply Decorators dynamically based on user selection
            switch (formatChoice) {
                case "2" -> displayContact = new UpperCaseDecorator(displayContact);
                case "3" -> displayContact = new MaskedEmailDecorator(displayContact);
                case "4" -> displayContact = new UpperCaseDecorator(new MaskedEmailDecorator(displayContact));
                case "1" -> {} // Standard, no decoration
                default -> {} // Default to standard
            }
            
            System.out.println(displayContact.getFormattedDetails()); 
        }
    }

    /**
     * Handles selecting and editing an existing contact.
     * @param contactService The service to update contacts
     * @param scanner The input scanner
     * @param loggedInUser The logged-in user
     */
    public static void editContactFlow(ContactService contactService, Scanner scanner, User loggedInUser) {
        System.out.println("\n-- Edit Contact --");
        List<Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to edit."); return; }
        for (int i = 0; i < userContacts.size(); i++) { System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName()); }
        System.out.print("\nEnter the number of the contact to edit: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= userContacts.size()) { System.out.println("Invalid selection."); return; }
            Contact selectedContact = userContacts.get(index);
            contactService.incrementContactViewCount(selectedContact); // Increment view count when opening for edit
            
            ContactEditInvoker invoker = new ContactEditInvoker();
            boolean editing = true;
            
            while (editing) {
                System.out.println("\nEditing: " + selectedContact.getName());
                System.out.println("1. Update Name");
                System.out.println("2. Add Phone Number");
                System.out.println("3. Add Email Address");
                System.out.println("4. Add Tag");
                System.out.println("5. Remove Tag");
                System.out.println("6. Undo Last Edit " + (invoker.canUndo() ? "(Available)" : ""));
                System.out.println("7. Redo Last Edit " + (invoker.canRedo() ? "(Available)" : ""));
                System.out.println("8. Done Editing");
                System.out.print("What would you like to update? ");
                
                String editChoice = scanner.nextLine();
                try {
                    switch (editChoice) {
                        case "1" -> {
                            System.out.print("Enter new name: ");
                            String newName = scanner.nextLine();
                            invoker.executeCommand(new ModifyContactCommand(selectedContact, contactService, c -> {
                                try { contactService.updateContactName(c, newName); } catch (ContactAppException e) { throw new RuntimeException(e); }
                            }));
                            System.out.println("Contact name updated.");
                        }
                        case "2" -> {
                            System.out.print("Enter new phone number: ");
                            String newPhone = scanner.nextLine();
                            invoker.executeCommand(new ModifyContactCommand(selectedContact, contactService, c -> {
                                try { contactService.addPhoneToContact(c, newPhone); } catch (ContactAppException e) { throw new RuntimeException(e); }
                            }));
                            System.out.println("Phone number added.");
                        }
                        case "3" -> {
                            System.out.print("Enter new email address: ");
                            String newEmail = scanner.nextLine();
                            invoker.executeCommand(new ModifyContactCommand(selectedContact, contactService, c -> {
                                try { contactService.addEmailToContact(c, newEmail); } catch (ContactAppException e) { throw new RuntimeException(e); }
                            }));
                            System.out.println("Email address added.");
                        }
                        case "4" -> {
                            System.out.print("Enter tag to add (e.g., Work, Family): ");
                            String tagStr = scanner.nextLine().trim();
                            if (tagStr.isEmpty()) {
                                System.out.println("Tag cannot be empty.");
                                break; // Use break to exit the switch case
                            }
                            invoker.executeCommand(new ModifyContactCommand(selectedContact, contactService, c -> {
                                try { contactService.addTagToContact(c, tagStr); } catch (ContactAppException e) { throw new RuntimeException(e); }
                            }));
                            System.out.println("Tag added.");
                        }
                        case "5" -> {
                            System.out.print("Enter tag to remove: ");
                            String tagStr = scanner.nextLine().trim();
                            if (tagStr.isEmpty()) {
                                System.out.println("Tag cannot be empty.");
                                break; // Use break to exit the switch case
                            }
                            invoker.executeCommand(new ModifyContactCommand(selectedContact, contactService, c -> {
                                try { contactService.removeTagFromContact(c, tagStr); } catch (ContactAppException e) { throw new RuntimeException(e); }
                            }));
                            System.out.println("Tag removed (if it existed).");
                        }
                        case "6" -> {
                            if (invoker.undoLastCommand()) System.out.println("Undo successful.");
                            else System.out.println("Nothing to undo.");
                        }
                        case "7" -> {
                            if (invoker.redoLastCommand()) System.out.println("Redo successful.");
                            else System.out.println("Nothing to redo.");
                        }
                        case "8" -> editing = false;
                        default -> System.out.println("Invalid option.");
                    }
                } catch (RuntimeException e) {
                    System.err.println("Edit Failed: " + e.getCause().getMessage());
                }
            }
        } catch (NumberFormatException e) { System.out.println("Please enter a valid number."); } 
    }

    /**
     * Handles selecting and deleting an existing contact.
     * @param contactService The service to delete contacts
     * @param scanner The input scanner
     * @param loggedInUser The logged-in user
     */
    public static void deleteContactFlow(ContactService contactService, Scanner scanner, User loggedInUser) {
        System.out.println("\n-- Delete Contact --");
        List<Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to delete."); return; }
        for (int i = 0; i < userContacts.size(); i++) { System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName()); }
        System.out.print("\nEnter the number of the contact to delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= userContacts.size()) { System.out.println("Invalid selection."); return; }
            Contact selectedContact = userContacts.get(index);
            
            System.out.println("\nSelected: " + selectedContact.getName());
            System.out.println("1. Soft Delete (Archive/Hide from view)");
            System.out.println("2. Hard Delete (Permanently Remove from Storage)");
            System.out.println("3. Cancel");
            System.out.print("Choose action: ");
            String choice = scanner.nextLine();
            
            boolean isHardDelete = false;
            
            if (choice.equals("3")) {
                System.out.println("Deletion cancelled.");
                return;
            } else if (choice.equals("2")) {
                isHardDelete = true;
                System.out.print("WARNING: Are you sure you want to PERMANENTLY delete '" + selectedContact.getName() + "'? (yes/no): ");
            } else if (choice.equals("1")) {
                System.out.print("Are you sure you want to softly delete '" + selectedContact.getName() + "'? (yes/no): ");
            } else {
                System.out.println("Invalid option. Cancelled.");
                return;
            }
            
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                if (contactService.deleteContact(selectedContact, isHardDelete)) {
                    System.out.println("Contact deleted successfully.");
                } else { 
                    System.out.println("Error: Could not delete the contact.");
                }
            } else { 
                System.out.println("Deletion cancelled."); 
            }
        } catch (NumberFormatException e) { System.out.println("Input Error: Please enter a valid numeric value."); }
    }

    /**
     * Displays all unique tags used by the user.
     * @param contactService The service to retrieve tags
     * @param loggedInUser The logged-in user
     */
    public static void manageTagsFlow(ContactService contactService, User loggedInUser) {
        System.out.println("\n-- Manage Tags --");
        java.util.Set<com.mycontactapp.tagging.Tag> uniqueTags = contactService.getAllUserTags(loggedInUser);
        if (uniqueTags.isEmpty()) {
            System.out.println("You have no tags currently in use.");
            System.out.println("Create and apply tags to your contacts in the 'Edit Contact' menu!");
        } else {
            System.out.println("Your Unique Tags:");
            for (com.mycontactapp.tagging.Tag tag : uniqueTags) {
                System.out.println(tag.toString());
            }
        }
    }
}