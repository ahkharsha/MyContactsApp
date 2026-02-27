package com.mycontactapp.main;

import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

import java.util.Scanner;

/**
 * MyContactsApp
 * This is the main file that runs the application.
 *
 * @author Developer
 * @version 1.0
 */
public class MyContactsApp {

    public static void main(String[] args) {
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Welcome to MyContacts App ===");

        while (running) {
            System.out.println("\n1. Register New User");
            System.out.println("2. Exit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerFlow(userService, scanner);
                    break;
                case "2":
                    running = false;
                    System.out.println("Exiting Application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
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
            System.out.println("User ID: " + newUser.getUserId());
            System.out.println("Account Type: " + newUser.getClass().getSimpleName());
            System.out.println("Contact Limit: " + newUser.getContactLimit());

        } catch (ContactAppException e) {
            System.err.println("Registration Failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred.");
        }
    }
}