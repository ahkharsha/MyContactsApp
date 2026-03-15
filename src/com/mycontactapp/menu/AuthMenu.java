package com.mycontactapp.menu;

import com.mycontactapp.auth.Authentication;
import com.mycontactapp.auth.BasicAuth;
import com.mycontactapp.auth.OAuth;
import com.mycontactapp.auth.SessionManager;
import com.mycontactapp.exception.ContactAppException;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.command.ChangePasswordCommand;
import com.mycontactapp.user.command.ProfileUpdateInvoker;
import com.mycontactapp.user.command.UpdateEmailCommand;
import com.mycontactapp.user.command.UpdateNameCommand;
import com.mycontactapp.user.model.User;

import java.util.Optional;
import java.util.Scanner;

/**
 * AuthMenu
 * Manages user authentication flows (register, login, profile).
 * @author Developer
 * @version 2.0
 */
public class AuthMenu {

    /**
     * Handles user registration input.
     * @param userService The service to use for registration
     * @param scanner The input scanner
     */
    public static void registerFlow(UserService userService, Scanner scanner) {
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
     * Handles user login input (Standard or OAuth).
     * @param userService The service to use for login
     * @param scanner The input scanner
     * @return An Optional containing the logged-in User if successful
     */
    public static Optional<User> loginFlow(UserService userService, Scanner scanner) {
        System.out.println("\n--- Login ---");
        System.out.println("1. Standard Login (Email & Password)");
        System.out.println("2. Continue with Google / GitHub (OAuth)");
        System.out.print("Choose an option: "); String authChoice = scanner.nextLine();
        System.out.print("Enter Email: "); String email = scanner.nextLine();
        Authentication authStrategy; String credential;
        
        switch (authChoice) {
            case "1" -> {
                System.out.print("Enter Password: ");
                credential = scanner.nextLine();
                authStrategy = new BasicAuth(userService);
            }
            case "2" -> {
                System.out.print("Enter Provider (Google/GitHub): ");
                credential = scanner.nextLine();
                authStrategy = new OAuth(userService);
            }
            default -> {
                System.out.println("Invalid authentication method selected.");
                return Optional.empty();
            }
        }
        
        Optional<User> authResult = authStrategy.authenticate(email, credential);
        if (authResult.isPresent()) {
            // Singleton session manager keeps login state independent from the UI controller.
            SessionManager.getInstance().login(authResult.get());
            System.out.println("\nLogin Successful! Welcome back, " + authResult.get().getFullName() + ".");
        } else {
            System.out.println("\nLogin Failed: Incorrect credentials or unregistered email.");
        }
        
        return authResult;
    }

    /**
     * Handles user profile update menu.
     * @param userService The service to use for updates
     * @param scanner The input scanner
     * @param loggedInUser The currently logged-in user
     * @return true if the user account was deleted, false otherwise
     */
    public static boolean profileMenu(UserService userService, Scanner scanner, User loggedInUser) {
        ProfileUpdateInvoker invoker = new ProfileUpdateInvoker();
        System.out.println("\n-- Profile Options --");
        System.out.println("1. Update Name");
        System.out.println("2. Change Password");
        System.out.println("3. Update Email");
        System.out.println("4. Delete Account");
        System.out.print("Choose option: ");
        String choice = scanner.nextLine();
        
        switch (choice) {
            case "1" -> {
                System.out.print("Enter new name: ");
                try {
                    invoker.executeCommand(new UpdateNameCommand(userService, loggedInUser, scanner.nextLine()));
                    System.out.println("Name updated.");
                } catch (ContactAppException e) {
                    System.err.println(e.getMessage());
                }
            }
            case "2" -> changePasswordFlow(userService, scanner, loggedInUser, invoker);
            case "3" -> {
                System.out.print("Enter new email: ");
                try {
                    invoker.executeCommand(new UpdateEmailCommand(userService, loggedInUser, scanner.nextLine()));
                    System.out.println("Email updated.");
                } catch (ContactAppException e) {
                    System.err.println(e.getMessage());
                }
            }
            case "4" -> {
                System.out.print("WARNING: Are you sure you want to delete your account? This cannot be undone. (yes/no): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    if (userService.deleteUserAccount(loggedInUser)) {
                        SessionManager.getInstance().logout();
                        System.out.println("Account deleted. Goodbye.");
                        return true;
                    } else {
                        System.err.println("Failed to delete account.");
                    }
                }
            }
            default -> System.out.println("Invalid option.");
        }
        return false;
    }

    /**
     * Handles the password change process for an authenticated user.
     * @param userService   The service to perform updates
     * @param scanner       The input scanner
     * @param loggedInUser  The logged-in user
     * @param invoker       The profile update invoker
     */
    private static void changePasswordFlow(UserService userService, Scanner scanner, User loggedInUser, ProfileUpdateInvoker invoker) {
        System.out.print("\nEnter your CURRENT password: "); String currentPassword = scanner.nextLine();
        System.out.print("Enter your NEW password (min 6 chars): "); String newPassword = scanner.nextLine();
        try {
            invoker.executeCommand(new ChangePasswordCommand(userService, loggedInUser, currentPassword, newPassword));
            System.out.println("Password changed successfully!");
        } catch (ContactAppException e) { System.err.println("Password Change Failed: " + e.getMessage()); }
    }
}