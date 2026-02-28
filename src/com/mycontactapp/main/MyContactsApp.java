package com.mycontactapp.main;

import com.mycontactapp.auth.Authentication;
import com.mycontactapp.auth.BasicAuth;
import com.mycontactapp.auth.OAuth;
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
 * @version 1.2
 */
public class MyContactsApp {

    // Simple session state management
    private static User loggedInUser = null;

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
                        System.out.println("Exiting Application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                // Logged-in Dashboard
                System.out.println("\n--- Dashboard (Welcome, " + loggedInUser.getFullName() + ") ---");
                System.out.println("1. Update Profile Name");
                System.out.println("2. Change Password");
                System.out.println("3. Logout");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        updateProfileFlow(userService, scanner);
                        break;
                    case "2":
                        changePasswordFlow(userService, scanner);
                        break;
                    case "3":
                        loggedInUser = null;
                        System.out.println("Logged out successfully.");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
        scanner.close();
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
     * Asks the user for a new name and saves it.
     */
    private static void updateProfileFlow(UserService userService, Scanner scanner) {
        System.out.print("\nEnter your new full name: ");
        String newName = scanner.nextLine();
        
        try {
            userService.updateUserProfile(loggedInUser, newName);
            System.out.println("Profile updated successfully! Your new name is: " + loggedInUser.getFullName());
        } catch (ContactAppException e) {
            System.err.println("Update Failed: " + e.getMessage());
        }
    }

    /**
     * Asks for the old password, checks it, and then saves the new password.
     */
    private static void changePasswordFlow(UserService userService, Scanner scanner) {
        System.out.print("\nEnter your CURRENT password: ");
        String currentPassword = scanner.nextLine();
        
        System.out.print("Enter your NEW password (min 6 chars): ");
        String newPassword = scanner.nextLine();
        
        try {
            userService.changeUserPassword(loggedInUser, currentPassword, newPassword);
            System.out.println("Password changed successfully!");
        } catch (ContactAppException e) {
            System.err.println("Password Change Failed: " + e.getMessage());
        }
    }
}