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
 * The main entry point and console user interface for the application.
 *
 * @author Developer
 * @version 5.0
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
                System.out.println("3. View Contacts"); // NEW OPTION
                System.out.println("4. Logout");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1": profileMenu(userService, scanner); break;
                    case "2": createContactFlow(scanner); break;
                    case "3": viewContactsFlow(); break; // NEW FLOW
                    case "4": loggedInUser = null; System.out.println("Logged out."); break;
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
     * Retrieves and displays all contacts for the logged-in user.
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
}