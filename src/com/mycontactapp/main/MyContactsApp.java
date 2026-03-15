package com.mycontactapp.main;

import com.mycontactapp.auth.SessionManager;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.menu.AuthMenu;
import com.mycontactapp.menu.ContactMenu;
import com.mycontactapp.menu.OperationsMenu;
import com.mycontactapp.search.FilterService;
import com.mycontactapp.search.SearchFilterService;
import com.mycontactapp.user.UserService;
import com.mycontactapp.user.model.User;

import java.util.Scanner;

/**
 * MyContactsApp
 * The main entry point and routing layer for the console application.
 * @author Developer
 * @version 13.0
 */
public class MyContactsApp {
    
    // Core application services
    private static final UserService userService = new UserService();
    private static final ContactService contactService = new ContactService();
    private static final SearchFilterService searchService = new SearchFilterService();
    private static final FilterService filterService = new FilterService();
    private static final SessionManager sessionManager = SessionManager.getInstance();

    /**
     * Application entry point.
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("=== Welcome to MyContacts App ===");

        while (running) {
            if (!sessionManager.isLoggedIn()) {
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. Register New User");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> AuthMenu.registerFlow(userService, scanner);
                    case "2" -> AuthMenu.loginFlow(userService, scanner);
                    case "3" -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid option.");
                }
            } else {
                User loggedInUser = sessionManager.getCurrentUser();
                System.out.println("\n--- Dashboard (" + loggedInUser.getFullName() + ") ---");
                System.out.println("1. Manage Profile");
                System.out.println("2. Add Contact");
                System.out.println("3. View Contacts");
                System.out.println("4. Edit Contact");
                System.out.println("5. Delete Contact");
                System.out.println("6. Bulk Operations");
                System.out.println("7. Search Contacts");
                System.out.println("8. Filter & Sort Contacts");
                System.out.println("9. Manage Tags"); 
                System.out.println("10. Logout");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        if (AuthMenu.profileMenu(userService, scanner, loggedInUser)) {
                            sessionManager.logout();
                        }
                    }
                    case "2" -> ContactMenu.createContactFlow(contactService, scanner, loggedInUser);
                    case "3" -> ContactMenu.viewContactsFlow(contactService, loggedInUser, scanner);
                    case "4" -> ContactMenu.editContactFlow(contactService, scanner, loggedInUser);
                    case "5" -> ContactMenu.deleteContactFlow(contactService, scanner, loggedInUser);
                    case "6" -> OperationsMenu.bulkOperationsFlow(contactService, scanner, loggedInUser);
                    case "7" -> OperationsMenu.searchContactsFlow(contactService, searchService, scanner, loggedInUser);
                    case "8" -> OperationsMenu.filterContactsFlow(contactService, filterService, scanner, loggedInUser);
                    case "9" -> ContactMenu.manageTagsFlow(contactService, loggedInUser);
                    case "10" -> {
                        sessionManager.logout();
                        System.out.println("Logged out.");
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        }
        scanner.close();
    }
}