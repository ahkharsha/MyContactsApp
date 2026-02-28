package com.mycontactapp.main;

import com.mycontactapp.auth.Authentication;
import com.mycontactapp.auth.BasicAuth;
import com.mycontactapp.auth.OAuth;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

import java.util.Optional;
import java.util.Scanner;

/**
 * MyContactsApp
 * This is the main file that runs the application.
 *
 * @author Developer
 * @version 7.0
 */
public class MyContactsApp {

    private static User loggedInUser = null;
    private static final ContactService contactService = new ContactService();

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
                    case "1": 
                        registerFlow(userService, scanner); 
                        break;
                    case "2": 
                        loginFlow(userService, scanner); 
                        break;
                    case "3": 
                        running = false; 
                        System.out.println("Goodbye!"); 
                        break;
                    default: 
                        System.out.println("Invalid option.");
                }
            } else {
                System.out.println("\n--- Dashboard (" + loggedInUser.getFullName() + ") ---");
                System.out.println("1. Manage Profile");
                System.out.println("2. Add Contact");
                System.out.println("3. View Contacts");
                System.out.println("4. Edit Contact");
                System.out.println("5. Delete Contact"); // NEW OPTION
                System.out.println("6. Logout");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": profileMenu(userService, scanner); break;
                    case "2": createContactFlow(scanner); break;
                    case "3": viewContactsFlow(); break; 
                    case "4": editContactFlow(scanner); break; 
                    case "5": deleteContactFlow(scanner); break; // NEW FLOW
                    case "6": loggedInUser = null; System.out.println("Logged out."); break;
                    default: System.out.println("Invalid option.");
                }
            }
        }
        scanner.close();
    }

    private static void profileMenu(UserService userService, Scanner scanner) {
        System.out.println("\n-- Profile Options --");
        System.out.println("1. Update Name");
        System.out.println("2. Change Password");
        System.out.print("Choose option: ");
        String choice = scanner.nextLine();
        
        if (choice.equals("1")) {
            System.out.print("Enter new name: ");
            try {
                userService.updateUserProfile(loggedInUser, scanner.nextLine());
                System.out.println("Name updated.");
            } catch (ContactAppException e) { 
                System.err.println(e.getMessage()); 
            }
        } else if (choice.equals("2")) {
            changePasswordFlow(userService, scanner);
        } else {
            System.out.println("Invalid option.");
        }
    }

    /**
     * Guides the user through creating a new contact (Person or Organization).
     */
    private static void createContactFlow(Scanner scanner) {
        System.out.println("\n-- Add Contact --");
        System.out.println("1. Add Person");
        System.out.println("2. Add Organization");
        System.out.print("Choose contact type: ");
        String type = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Phone (optional): ");
        String phone = scanner.nextLine();
        System.out.print("Email (optional): ");
        String email = scanner.nextLine();

        try {
            if (type.equals("1")) {
                System.out.print("Relationship (e.g. Friend, Brother): ");
                contactService.createPersonContact(loggedInUser, name, phone, email, scanner.nextLine());
                System.out.println("Person Contact added successfully!");
            } else if (type.equals("2")) {
                System.out.print("Website: ");
                contactService.createOrganizationContact(loggedInUser, name, phone, email, scanner.nextLine());
                System.out.println("Organization Contact added successfully!");
            } else {
                System.out.println("Invalid type selected.");
            }
        } catch (ContactAppException e) {
            System.err.println("Failed to add contact: " + e.getMessage());
        }
    }

    private static void changePasswordFlow(UserService userService, Scanner scanner) {
        System.out.print("\nEnter your CURRENT password: ");
        String currentPassword = scanner.nextLine();
        
        try {
            // FIXED: Validating old password immediately before asking for new one
            userService.verifyCurrentPassword(loggedInUser, currentPassword);
            
            System.out.print("Enter your NEW password (min 6 chars): ");
            String newPassword = scanner.nextLine();
            
            userService.changeUserPassword(loggedInUser, newPassword);
            System.out.println("Password changed successfully!");
        } catch (ContactAppException e) {
            System.err.println("Password Change Failed: " + e.getMessage());
        }
    }

    /**
     * Handles the console prompts for user registration.
     */
    private static void registerFlow(UserService userService, Scanner scanner) {
        try {
            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter Email: ");
            String email = scanner.nextLine();

            System.out.print("Enter Password (min 6 chars): ");
            String password = scanner.nextLine();

            System.out.print("Do you want a Premium account? (yes/no): ");
            boolean isPremium = scanner.nextLine().trim().equalsIgnoreCase("yes");

            User newUser = userService.registerUser(email, password, name, isPremium);
            
            System.out.println("\nRegistration Successful!");
            System.out.println("Account Type: " + newUser.getClass().getSimpleName());
            System.out.println("Contact Limit: " + newUser.getContactLimit());

        } catch (ContactAppException e) {
            System.err.println("Registration Failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred.");
        }
    }

    /**
     * Handles the console prompts for user authentication demonstrating polymorphism.
     */
    private static void loginFlow(UserService userService, Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.println("1. Standard Login (Email & Password)");
        System.out.println("2. Continue with Google / GitHub (OAuth)");
        System.out.print("Choose an option: ");
        String authChoice = scanner.nextLine();

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();

        Authentication authStrategy;
        String credential;

        if (authChoice.equals("1")) {
            System.out.print("Enter Password: ");
            credential = scanner.nextLine();
            authStrategy = new BasicAuth(userService);
        } else if (authChoice.equals("2")) {
            System.out.print("Enter Provider (Google/GitHub): ");
            credential = scanner.nextLine();
            authStrategy = new OAuth(userService);
        } else {
            System.out.println("Invalid authentication method selected.");
            return;
        }

        Optional<User> authResult = authStrategy.authenticate(email, credential);

        if (authResult.isPresent()) {
            loggedInUser = authResult.get();
            System.out.println("\nLogin Successful! Welcome back, " + loggedInUser.getFullName() + ".");
        } else {
            System.out.println("\nLogin Failed: Incorrect credentials or unregistered email.");
        }
    }
    
    /**
     * Shows a list of all contacts for the current user.
     */
    private static void viewContactsFlow() {
        System.out.println("\n-- My Contacts --");
        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        
        if (userContacts.isEmpty()) {
            System.out.println("You have no contacts saved yet.");
        } else {
            for (int i = 0; i < userContacts.size(); i++) {
                System.out.println("\n[" + (i + 1) + "]");
                System.out.println(userContacts.get(i).getFormattedDetails());
            }
        }
    }
    
    /**
     * Guides the user through editing an existing contact.
     * Shows a list, asks for selection, and updates the contact.
     *
     * @param scanner The console input scanner
     */
    private static void editContactFlow(Scanner scanner) {
        System.out.println("\n-- Edit Contact --");
        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        
        if (userContacts.isEmpty()) {
            System.out.println("You have no contacts to edit.");
            return;
        }

        // Display a simplified list for selection
        for (int i = 0; i < userContacts.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName());
        }

        System.out.print("\nEnter the number of the contact to edit: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= userContacts.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            com.mycontactapp.contact.Contact selectedContact = userContacts.get(index);
            
            System.out.println("\nEditing: " + selectedContact.getName());
            System.out.println("1. Update Name");
            System.out.println("2. Add Phone Number");
            System.out.println("3. Add Email Address");
            System.out.print("What would you like to update? ");
            
            String editChoice = scanner.nextLine();
            
            if (editChoice.equals("1")) {
                System.out.print("Enter new name: ");
                contactService.updateContactName(selectedContact, scanner.nextLine());
                System.out.println("Contact name updated.");
            } else if (editChoice.equals("2")) {
                System.out.print("Enter new phone number: ");
                contactService.addPhoneToContact(selectedContact, scanner.nextLine());
                System.out.println("Phone number added.");
            } else if (editChoice.equals("3")) {
                System.out.print("Enter new email address: ");
                contactService.addEmailToContact(selectedContact, scanner.nextLine());
                System.out.println("Email address added.");
            } else {
                System.out.println("Invalid option.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (ContactAppException e) {
            System.err.println("Edit Failed: " + e.getMessage());
        }
    }
    
    /**
     * Handles the console prompts for deleting a contact.
     * Implements a safety confirmation dialog and exception handling for user input.
     *
     * @param scanner The console input scanner
     */
    private static void deleteContactFlow(Scanner scanner) {
        System.out.println("\n-- Delete Contact --");
        java.util.List<com.mycontactapp.contact.Contact> userContacts = contactService.getUserContacts(loggedInUser);
        
        if (userContacts.isEmpty()) {
            System.out.println("You have no contacts to delete.");
            return;
        }

        // Display a simplified list for selection
        for (int i = 0; i < userContacts.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName());
        }

        System.out.print("\nEnter the number of the contact to permanently delete: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index < 0 || index >= userContacts.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            com.mycontactapp.contact.Contact selectedContact = userContacts.get(index);
            
            // Required Confirmation Dialog
            System.out.print("WARNING: Are you sure you want to permanently delete '" 
                    + selectedContact.getName() + "'? (yes/no): ");
            String confirm = scanner.nextLine().trim();
            
            if (confirm.equalsIgnoreCase("yes")) {
                boolean success = contactService.deleteContact(selectedContact);
                if (success) {
                    System.out.println("Contact deleted successfully.");
                } else {
                    System.out.println("Error: Could not delete the contact.");
                }
            } else {
                System.out.println("Deletion cancelled. The contact is safe.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Input Error: Please enter a valid numeric value.");
        }
    }
}