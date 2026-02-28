package com.mycontactapp.main;

import com.mycontactapp.auth.Authentication;
import com.mycontactapp.auth.BasicAuth;
import com.mycontactapp.auth.OAuth;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.search.FilterService;
import com.mycontactapp.search.SearchFilterService;
import com.mycontactapp.search.SearchFilterInterface;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

import java.util.Optional;
import java.util.Scanner;

/**
 * MyContactsApp
 * The main entry point and console user interface for the application.
 *
 * @author Developer
 * @version 11.0
 */
public class MyContactsApp {

    private static User loggedInUser = null;
    private static final ContactService contactService = new ContactService();
    private static final SearchFilterService searchService = new SearchFilterService();
    private static final FilterService filterService = new FilterService();

    /**
     * Main method that drives the console application.
     * Continuously prompts the user with menu options until exit.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Welcome to MyContacts App ===");

        while (running) {
            if (loggedInUser == null) {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. Register New User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1": registerFlow(userService, scanner); break;
                    case "2": loginFlow(userService, scanner); break;
                    case "3": running = false; System.out.println("Goodbye!"); break;
                    default: System.out.println("Invalid option.");
                }
            } else {
                System.out.println("\n--- Dashboard (" + loggedInUser.getFullName() + ") ---");
                System.out.println("1. Manage Profile");
                System.out.println("2. Add Contact");
                System.out.println("3. View Contacts");
                System.out.println("4. Edit Contact");
                System.out.println("5. Delete Contact");
                System.out.println("6. Bulk Operations");
                System.out.println("7. Search Contacts");
                System.out.println("8. Filter & Sort Contacts");
                System.out.println("9. Manage Tags"); // NEW OPTION
                System.out.println("10. Logout");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": profileMenu(userService, scanner); break;
                    case "2": createContactFlow(scanner); break;
                    case "3": viewContactsFlow(); break; 
                    case "4": editContactFlow(scanner); break; 
                    case "5": deleteContactFlow(scanner); break;
                    case "6": bulkOperationsFlow(scanner); break; 
                    case "7": searchContactsFlow(scanner); break; 
                    case "8": filterContactsFlow(scanner); break;
                    case "9": manageTagsFlow(); break; // NEW FLOW
                    case "10": loggedInUser = null; System.out.println("Logged out."); break;
                    default: System.out.println("Invalid option.");
                }
            }
        }
        scanner.close();
    }

    /**
     * Handles the flow for viewing user's unique tags.
     */
    private static void manageTagsFlow() {
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

    /**
     * Handles the flow for filtering and sorting contacts.
     *
     * @param scanner The scanner to read user input
     */
    private static void filterContactsFlow(Scanner scanner) {
        System.out.println("\n-- Filter & Sort Contacts --");
        System.out.println("1. Sort Alphabetically (by Name)");
        System.out.println("2. Sort by Date Added (Oldest to Newest)");
        System.out.println("3. Sort by Date Added (Newest to Oldest)");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to sort."); return; }

        java.util.List<com.mycontactapp.contact.Contact> sortedResults;
        
        if (choice.equals("1")) sortedResults = filterService.sortAlphabetically(userContacts);
        else if (choice.equals("2")) sortedResults = filterService.sortByDateAdded(userContacts, true);
        else if (choice.equals("3")) sortedResults = filterService.sortByDateAdded(userContacts, false);
        else { System.out.println("Invalid option selected."); return; }

        System.out.println("\nFiltered & Sorted Contacts:");
        for (int i = 0; i < sortedResults.size(); i++) {
            System.out.println("\n[" + (i + 1) + "]");
            System.out.println(sortedResults.get(i).getFormattedDetails());
        }
    }

    /**
     * Handles the flow for searching contacts.
     *
     * @param scanner The scanner to read user input
     */
    private static void searchContactsFlow(Scanner scanner) {
        System.out.println("\n-- Search Contacts --");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Phone");
        System.out.println("3. Search by Email");
        System.out.print("Choose search type: ");
        String searchType = scanner.nextLine();

        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        SearchFilterInterface criteria;

        if (searchType.equals("1")) { criteria = new SearchFilterService.NameSearch(); } 
        else if (searchType.equals("2")) { criteria = new SearchFilterService.PhoneSearch(); } 
        else if (searchType.equals("3")) { criteria = new SearchFilterService.EmailSearch(); } 
        else { System.out.println("Invalid search type selected."); return; }

        java.util.List<com.mycontactapp.contact.Contact> results = searchService.search(userContacts, query, criteria);

        System.out.println("\nSearch Results (" + results.size() + " found):");
        if (results.isEmpty()) { System.out.println("No contacts match your query."); } 
        else { for (int i = 0; i < results.size(); i++) { System.out.println("\n[" + (i + 1) + "]"); System.out.println(results.get(i).getFormattedDetails()); } }
    }

    /**
     * Handles bulk operations like deleting multiple contacts or exporting data.
     *
     * @param scanner The scanner to read user input
     */
    private static void bulkOperationsFlow(Scanner scanner) {
        System.out.println("\n-- Bulk Operations --");
        System.out.println("1. Bulk Delete Contacts");
        System.out.println("2. Export All Contacts to CSV");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to perform operations on."); return; }

        if (choice.equals("1")) {
            System.out.println("\nSelect contacts to delete (enter numbers separated by commas, e.g., 1,3,4): ");
            for (int i = 0; i < userContacts.size(); i++) { System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName()); }
            System.out.print("Selection: ");
            String[] selections = scanner.nextLine().split(",");
            
            java.util.List<com.mycontactapp.contact.Contact> toDelete = new java.util.ArrayList<>();
            for (String sel : selections) {
                try {
                    int index = Integer.parseInt(sel.trim()) - 1;
                    if (index >= 0 && index < userContacts.size()) { toDelete.add(userContacts.get(index)); }
                } catch (NumberFormatException e) { }
            }
            
            if (toDelete.isEmpty()) { System.out.println("No valid contacts selected."); return; }
            System.out.print("WARNING: Permanently delete " + toDelete.size() + " contacts? (yes/no): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                int deletedCount = contactService.bulkDeleteContacts(toDelete);
                System.out.println("Successfully deleted " + deletedCount + " contacts.");
            } else { System.out.println("Bulk deletion cancelled."); }

        } else if (choice.equals("2")) {
            System.out.print("Enter export filename (e.g., my_contacts.csv): ");
            String filename = scanner.nextLine().trim();
            if (filename.isEmpty()) filename = "exported_contacts.csv";
            
            if (com.mycontactapp.util.FileHandler.exportContactsToCSV(userContacts, filename)) {
                System.out.println("Contacts successfully exported to " + filename);
            }
        } else { System.out.println("Invalid option."); }
    }

    /**
     * Handles the flow for deleting a single contact.
     *
     * @param scanner The scanner to read user input
     */
    private static void deleteContactFlow(Scanner scanner) {
        System.out.println("\n-- Delete Contact --");
        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to delete."); return; }
        for (int i = 0; i < userContacts.size(); i++) { System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName()); }
        System.out.print("\nEnter the number of the contact to permanently delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= userContacts.size()) { System.out.println("Invalid selection."); return; }
            com.mycontactapp.contact.Contact selectedContact = userContacts.get(index);
            System.out.print("WARNING: Are you sure you want to permanently delete '" + selectedContact.getName() + "'? (yes/no): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                if (contactService.deleteContact(selectedContact)) System.out.println("Contact deleted successfully.");
                else System.out.println("Error: Could not delete the contact.");
            } else { System.out.println("Deletion cancelled."); }
        } catch (NumberFormatException e) { System.out.println("Input Error: Please enter a valid numeric value."); }
    }

    /**
     * Handles the flow for editing an existing contact's details.
     *
     * @param scanner The scanner to read user input
     */
    private static void editContactFlow(Scanner scanner) {
        System.out.println("\n-- Edit Contact --");
        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to edit."); return; }
        for (int i = 0; i < userContacts.size(); i++) { System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName()); }
        System.out.print("\nEnter the number of the contact to edit: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= userContacts.size()) { System.out.println("Invalid selection."); return; }
            com.mycontactapp.contact.Contact selectedContact = userContacts.get(index);
            System.out.println("\nEditing: " + selectedContact.getName());
            System.out.println("1. Update Name");
            System.out.println("2. Add Phone Number");
            System.out.println("3. Add Email Address");
            System.out.print("What would you like to update? ");
            String editChoice = scanner.nextLine();
            if (editChoice.equals("1")) {
                System.out.print("Enter new name: "); contactService.updateContactName(selectedContact, scanner.nextLine()); System.out.println("Contact name updated.");
            } else if (editChoice.equals("2")) {
                System.out.print("Enter new phone number: "); contactService.addPhoneToContact(selectedContact, scanner.nextLine()); System.out.println("Phone number added.");
            } else if (editChoice.equals("3")) {
                System.out.print("Enter new email address: "); contactService.addEmailToContact(selectedContact, scanner.nextLine()); System.out.println("Email address added.");
            } else { System.out.println("Invalid option."); }
        } catch (NumberFormatException e) { System.out.println("Please enter a valid number."); } catch (ContactAppException e) { System.err.println("Edit Failed: " + e.getMessage()); }
    }

    /**
     * Displays all contacts for the logged-in user.
     */
    private static void viewContactsFlow() {
        System.out.println("\n-- My Contacts --");
        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts saved yet."); } 
        else { for (int i = 0; i < userContacts.size(); i++) { System.out.println("\n[" + (i + 1) + "]"); System.out.println(userContacts.get(i).getFormattedDetails()); } }
    }

    /**
     * Handles the user profile management menu.
     *
     * @param userService The service for user-related operations
     * @param scanner     The scanner to read user input
     */
    private static void profileMenu(UserService userService, Scanner scanner) {
        System.out.println("\n-- Profile Options --");
        System.out.println("1. Update Name");
        System.out.println("2. Change Password");
        System.out.print("Choose option: ");
        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            System.out.print("Enter new name: ");
            try { userService.updateUserProfile(loggedInUser, scanner.nextLine()); System.out.println("Name updated."); } catch (ContactAppException e) { System.err.println(e.getMessage()); }
        } else if (choice.equals("2")) { changePasswordFlow(userService, scanner); } else { System.out.println("Invalid option."); }
    }

    /**
     * Handles the flow for creating a new contact (Person or Organization).
     *
     * @param scanner The scanner to read user input
     */
    private static void createContactFlow(Scanner scanner) {
        System.out.println("\n-- Add Contact --");
        System.out.println("1. Add Person");
        System.out.println("2. Add Organization");
        System.out.print("Choose contact type: ");
        String type = scanner.nextLine();
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Phone (optional): "); String phone = scanner.nextLine();
        System.out.print("Email (optional): "); String email = scanner.nextLine();
        try {
            if (type.equals("1")) {
                System.out.print("Relationship (e.g. Friend, Brother): "); contactService.createPersonContact(loggedInUser, name, phone, email, scanner.nextLine()); System.out.println("Person Contact added successfully!");
            } else if (type.equals("2")) {
                System.out.print("Website: "); contactService.createOrganizationContact(loggedInUser, name, phone, email, scanner.nextLine()); System.out.println("Organization Contact added successfully!");
            } else { System.out.println("Invalid type selected."); }
        } catch (ContactAppException e) { System.err.println("Failed to add contact: " + e.getMessage()); }
    }

    /**
     * Handles the flow for changing the logged-in user's password.
     *
     * @param userService The service for user-related operations
     * @param scanner     The scanner to read user input
     */
    private static void changePasswordFlow(UserService userService, Scanner scanner) {
        System.out.print("\nEnter your CURRENT password: "); String currentPassword = scanner.nextLine();
        try {
            userService.verifyCurrentPassword(loggedInUser, currentPassword);
            System.out.print("Enter your NEW password (min 6 chars): ");
            userService.changeUserPassword(loggedInUser, scanner.nextLine());
            System.out.println("Password changed successfully!");
        } catch (ContactAppException e) { System.err.println("Password Change Failed: " + e.getMessage()); }
    }

    /**
     * Handles the registration of a new user.
     *
     * @param userService The service for user-related operations
     * @param scanner     The scanner to read user input
     */
    private static void registerFlow(UserService userService, Scanner scanner) {
        try {
            System.out.print("Enter Full Name: "); String name = scanner.nextLine();
            System.out.print("Enter Email: "); String email = scanner.nextLine();
            System.out.print("Enter Password (min 6 chars): "); String password = scanner.nextLine();
            System.out.print("Do you want a Premium account? (yes/no): "); boolean isPremium = scanner.nextLine().trim().equalsIgnoreCase("yes");
            User newUser = userService.registerUser(email, password, name, isPremium);
            System.out.println("\nRegistration Successful!");
            System.out.println("Account Type: " + newUser.getClass().getSimpleName());
            System.out.println("Contact Limit: " + newUser.getContactLimit());
        } catch (ContactAppException e) { System.err.println("Registration Failed: " + e.getMessage()); } catch (Exception e) { System.err.println("An unexpected error occurred."); }
    }

    /**
     * Handles the user login process.
     *
     * @param userService The service for user-related operations
     * @param scanner     The scanner to read user input
     */
    private static void loginFlow(UserService userService, Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.println("1. Standard Login (Email & Password)");
        System.out.println("2. Continue with Google / GitHub (OAuth)");
        System.out.print("Choose an option: "); String authChoice = scanner.nextLine();
        System.out.print("Enter Email: "); String email = scanner.nextLine();
        Authentication authStrategy; String credential;
        if (authChoice.equals("1")) { System.out.print("Enter Password: "); credential = scanner.nextLine(); authStrategy = new BasicAuth(userService); } 
        else if (authChoice.equals("2")) { System.out.print("Enter Provider (Google/GitHub): "); credential = scanner.nextLine(); authStrategy = new OAuth(userService); } 
        else { System.out.println("Invalid authentication method selected."); return; }
        Optional<User> authResult = authStrategy.authenticate(email, credential);
        if (authResult.isPresent()) { loggedInUser = authResult.get(); System.out.println("\nLogin Successful! Welcome back, " + loggedInUser.getFullName() + "."); } 
        else { System.out.println("\nLogin Failed: Incorrect credentials or unregistered email."); }
    }
}