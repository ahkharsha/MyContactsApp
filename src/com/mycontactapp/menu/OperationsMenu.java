package com.mycontactapp.menu;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.ContactService;
import com.mycontactapp.contact.ContactGroup;
import com.mycontactapp.search.FilterService;
import com.mycontactapp.search.SearchFilterService;
import com.mycontactapp.search.SearchFilterInterface;
import com.mycontactapp.user.model.User;
import com.mycontactapp.util.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * OperationsMenu
 * Manages bulk operations and search/filter flows.
 * @author Developer
 * @version 1.0
 */
public class OperationsMenu {

    /**
     * Handles bulk delete and export flows.
     * @param contactService The service to retrieve and delete contacts
     * @param scanner The input scanner
     * @param loggedInUser The logged-in user
     */
    public static void bulkOperationsFlow(ContactService contactService, Scanner scanner, User loggedInUser) {
        System.out.println("\n-- Bulk Operations --");
        System.out.println("1. Bulk Delete Contacts");
        System.out.println("2. Export All Contacts to CSV");
        System.out.println("3. Bulk Tag Contacts");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        List<Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to perform operations on."); return; }

        switch (choice) {
            case "1" -> {
                System.out.println("\nSelect contacts to delete (enter numbers separated by commas, e.g., 1,3,4): ");
                for (int i = 0; i < userContacts.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName());
                }
                System.out.print("Selection: ");
                String[] selections = scanner.nextLine().split(",");

                List<Contact> toDelete = new ArrayList<>();
                for (String sel : selections) {
                    try {
                        int index = Integer.parseInt(sel.trim()) - 1;
                        if (index >= 0 && index < userContacts.size()) {
                            toDelete.add(userContacts.get(index));
                        }
                    } catch (NumberFormatException e) {
                    }
                }

                if (toDelete.isEmpty()) {
                    System.out.println("No valid contacts selected.");
                    return;
                }
                
                ContactGroup group = new ContactGroup("Bulk Delete Target");
                for (Contact c : toDelete) {
                    group.add(c);
                }
                
                System.out.print("WARNING: Soft delete " + toDelete.size() + " contacts? (yes/no): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    group.setActive(false);
                    contactService.saveAllContacts();
                    System.out.println("Successfully deleted contacts.");
                } else {
                    System.out.println("Bulk deletion cancelled.");
                }

            }
            case "2" -> {
                System.out.print("Enter export filename (e.g., my_contacts.csv): ");
                String filename = scanner.nextLine().trim();
                if (filename.isEmpty()) filename = "exported_contacts.csv";

                ContactGroup allContactsGroup = new ContactGroup("Export Group");
                for (Contact c : userContacts) {
                    allContactsGroup.add(c);
                }

                if (FileHandler.exportContactsToCSV(allContactsGroup.getAsContactList(), filename)) {
                    System.out.println("Contacts successfully exported to " + filename);
                }
            }
            case "3" -> {
                System.out.println("\nSelect contacts to tag (enter numbers separated by commas): ");
                for (int i = 0; i < userContacts.size(); i++) {
                    System.out.println("[" + (i + 1) + "] " + userContacts.get(i).getName());
                }
                System.out.print("Selection: ");
                String[] selections = scanner.nextLine().split(",");

                List<Contact> toTag = new ArrayList<>();
                for (String sel : selections) {
                    try {
                        int index = Integer.parseInt(sel.trim()) - 1;
                        if (index >= 0 && index < userContacts.size()) {
                            toTag.add(userContacts.get(index));
                        }
                    } catch (NumberFormatException e) {
                    }
                }

                if (toTag.isEmpty()) {
                    System.out.println("No valid contacts selected.");
                    return;
                }
                System.out.print("Enter tag to apply: ");
                String newTag = scanner.nextLine().trim();

                ContactGroup tagGroup = new ContactGroup("Bulk Tag Target");
                for (Contact c : toTag) {
                    tagGroup.add(c);
                }

                try {
                    tagGroup.addTag(new com.mycontactapp.tagging.Tag(newTag));
                    contactService.saveAllContacts();
                    System.out.println("Successfully tagged " + toTag.size() + " contacts.");
                } catch (Exception e) {
                    System.err.println("Failed to tag group: " + e.getMessage());
                }
            }
            default -> System.out.println("Invalid option.");
        }
    }

    /**
     * Handles searching for contacts using various criteria.
     * @param contactService The service to retrieve contacts
     * @param searchService The service to perform search logic
     * @param scanner The input scanner
     * @param loggedInUser The logged-in user
     */
    public static void searchContactsFlow(ContactService contactService, SearchFilterService searchService, Scanner scanner, User loggedInUser) {
        System.out.println("\n-- Search Contacts --");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Phone");
        System.out.println("3. Search by Email");
        System.out.println("4. Search by Tag"); 
        System.out.print("Choose search type: ");
        String searchType = scanner.nextLine();

        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        List<Contact> userContacts = contactService.getUserContacts(loggedInUser);
        SearchFilterInterface criteria;

        switch (searchType) {
            case "1" -> criteria = new SearchFilterService.NameSearch();
            case "2" -> criteria = new SearchFilterService.PhoneSearch();
            case "3" -> criteria = new SearchFilterService.EmailSearch();
            case "4" -> criteria = new SearchFilterService.TagSearch();
            default -> {
                System.out.println("Invalid search type selected.");
                return;
            }
        }

        List<Contact> results = searchService.search(userContacts, query, criteria);

        System.out.println("\nSearch Results (" + results.size() + " found):");
        if (results.isEmpty()) { System.out.println("No contacts match your query."); } 
        else { 
            for (int i = 0; i < results.size(); i++) { 
                contactService.incrementContactViewCount(results.get(i)); 
                System.out.println("\n[" + (i + 1) + "]"); 
                System.out.println(results.get(i).getFormattedDetails()); 
            } 
        }
    }

    /**
     * Handles filtering and sorting contacts.
     * @param contactService The service to retrieve contacts
     * @param filterService The service to perform sorting
     * @param scanner The input scanner
     * @param loggedInUser The logged-in user
     */
    public static void filterContactsFlow(ContactService contactService, FilterService filterService, Scanner scanner, User loggedInUser) {
        System.out.println("\n-- Filter & Sort Contacts --");
        System.out.println("1. Sort Alphabetically (by Name)");
        System.out.println("2. Sort by Date Added (Oldest to Newest)");
        System.out.println("3. Sort by Date Added (Newest to Oldest)");
        System.out.println("4. Sort by Frequently Contacted (Most Viewed)");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        List<Contact> userContacts = contactService.getUserContacts(loggedInUser);
        if (userContacts.isEmpty()) { System.out.println("You have no contacts to sort."); return; }

        List<Contact> sortedResults;
        
        switch (choice) {
            case "1" -> sortedResults = filterService.sortAlphabetically(userContacts);
            case "2" -> sortedResults = filterService.sortByDateAdded(userContacts, true);
            case "3" -> sortedResults = filterService.sortByDateAdded(userContacts, false);
            case "4" -> sortedResults = filterService.sortByFrequentlyContacted(userContacts);
            default -> {
                System.out.println("Invalid option selected.");
                return;
            }
        }

        System.out.println("\nFiltered & Sorted Contacts:");
        for (int i = 0; i < sortedResults.size(); i++) {
            System.out.println("\n[" + (i + 1) + "]");
            System.out.println(sortedResults.get(i).getFormattedDetails());
        }
    }
}