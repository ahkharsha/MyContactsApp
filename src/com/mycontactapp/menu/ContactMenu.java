package com.mycontactapp.menu;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.contact.decorator.ContactDisplay;
import com.mycontactapp.contact.decorator.MaskedEmailDecorator;
import com.mycontactapp.contact.decorator.UpperCaseDecorator;
import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.model.User;

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
            
            System.out.println("\nEditing: " + selectedContact.getName());
            System.out.println("1. Update Name");
            System.out.println("2. Add Phone Number");
            System.out.println("3. Add Email Address");
            System.out.println("4. Add Tag");
            System.out.println("5. Remove Tag");
            System.out.print("What would you like to update? ");
            
            String editChoice = scanner.nextLine();
            switch (editChoice) {
                case "1" -> {
                    System.out.print("Enter new name: ");
                    contactService.updateContactName(selectedContact, scanner.nextLine());
                    System.out.println("Contact name updated.");
                }
                case "2" -> {
                    System.out.print("Enter new phone number: ");
                    contactService.addPhoneToContact(selectedContact, scanner.nextLine());
                    System.out.println("Phone number added.");
                }
                case "3" -> {
                    System.out.print("Enter new email address: ");
                    contactService.addEmailToContact(selectedContact, scanner.nextLine());
                    System.out.println("Email address added.");
                }
                case "4" -> {
                    System.out.print("Enter tag to add (e.g., Work, Family): ");
                    contactService.addTagToContact(selectedContact, scanner.nextLine());
                    System.out.println("Tag added.");
                }
                case "5" -> {
                    System.out.print("Enter tag to remove: ");
                    contactService.removeTagFromContact(selectedContact, scanner.nextLine());
                    System.out.println("Tag removed (if it existed).");
                }
                default -> System.out.println("Invalid option.");
            }
        } catch (NumberFormatException e) { System.out.println("Please enter a valid number."); } catch (ContactAppException e) { System.err.println("Edit Failed: " + e.getMessage()); }
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
        System.out.print("\nEnter the number of the contact to permanently delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= userContacts.size()) { System.out.println("Invalid selection."); return; }
            Contact selectedContact = userContacts.get(index);
            System.out.print("WARNING: Are you sure you want to permanently delete '" + selectedContact.getName() + "'? (yes/no): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                if (contactService.deleteContact(selectedContact)) System.out.println("Contact deleted successfully.");
                else System.out.println("Error: Could not delete the contact.");
            } else { System.out.println("Deletion cancelled."); }
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