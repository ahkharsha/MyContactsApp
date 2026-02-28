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
 * @version 1.1
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
                System.out.println("1. Logout");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                if (choice.equals("1")) {
                    loggedInUser = null;
                    System.out.println("Logged out successfully.");
                } else {
                    System.out.println("More features coming soon!");
                }
            }
        }
        scanner.close();
    }

    /**
     * Handles the console prompts for user registration.
     *
     * @param userService The service handling business logic
     * @param scanner     The console input scanner
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
     * Handles the user login process.
     * Asks the user if they want to use Password or Google/GitHub.
     *
     * @param userService The service handling business logic
     * @param scanner     The console input scanner
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

        // Polymorphic authentication execution
        Optional<User> authResult = authStrategy.authenticate(email, credential);

        if (authResult.isPresent()) {
            loggedInUser = authResult.get();
            System.out.println("\nLogin Successful! Welcome back, " + loggedInUser.getFullName() + ".");
        } else {
            System.out.println("\nLogin Failed: Incorrect credentials or unregistered email.");
        }
    }
}