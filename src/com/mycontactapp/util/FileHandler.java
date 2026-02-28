package com.mycontactapp.util;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Organization;
import com.mycontactapp.contact.Person;
import com.mycontactapp.user.model.FreeUser;
import com.mycontactapp.user.model.PremiumUser;
import com.mycontactapp.user.model.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FileHandler
 * Utility class for basic file I/O to persist users and contacts to .txt files.
 *
 * @author Developer
 * @version 2.0
 */
public class FileHandler {

    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String CONTACTS_FILE = DATA_DIR + "/contacts.txt";
    private static final String DELIMITER = "\\|"; // Using pipe as delimiter

    static {
        new File(DATA_DIR).mkdirs(); // Ensure directory exists
    }

    /**
     * Saves the list of registered users to a text file.
     * @param users The list of users to save
     */
    public static void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                String type = u instanceof PremiumUser ? "Premium" : "Free";
                writer.println(type + "|" + u.getUserId() + "|" + u.getEmail() + "|" + u.getPasswordHash() + "|" + u.getFullName());
            }
        } catch (IOException e) {
            System.err.println("Error saving users to file.");
        }
    }

    /**
     * Loads registered users from the text file.
     * @return A list of populated User objects
     */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (parts.length == 5) {
                    User user = parts[0].equals("Premium") 
                        ? new PremiumUser(parts[2], parts[3], parts[4], parts[1]) 
                        : new FreeUser(parts[2], parts[3], parts[4], parts[1]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users.");
        }
        return users;
    }

    /**
     * Saves the list of contacts to a text file.
     * @param contacts The list of contacts to save
     */
    public static void saveContacts(List<Contact> contacts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONTACTS_FILE))) {
            for (Contact c : contacts) {
                String type = c instanceof Person ? "Person" : "Organization";
                String phones = String.join(",", c.getPhoneNumbers());
                String emails = String.join(",", c.getEmailAddresses());
                String extra = c instanceof Person ? ((Person) c).getRelationship() : ((Organization) c).getWebsite();
                
                writer.println(type + "|" + c.getContactId() + "|" + c.getUserId() + "|" + c.getName() + "|" 
                        + phones + "|" + emails + "|" + c.getCreatedAt().toString() + "|" + extra);
            }
        } catch (IOException e) {
            System.err.println("Error saving contacts to file.");
        }
    }

    /**
     * Loads contacts from the text file.
     * @return A list of populated Contact objects
     */
    public static List<Contact> loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        File file = new File(CONTACTS_FILE);
        if (!file.exists()) return contacts;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(DELIMITER, -1);
                if (p.length == 8) {
                    List<String> phones = p[4].isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(p[4].split(",")));
                    List<String> emails = p[5].isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(p[5].split(",")));
                    LocalDateTime createdAt = LocalDateTime.parse(p[6]);

                    if (p[0].equals("Person")) {
                        contacts.add(new Person(p[1], p[2], p[3], phones, emails, createdAt, p[7]));
                    } else {
                        contacts.add(new Organization(p[1], p[2], p[3], phones, emails, createdAt, p[7]));
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading contacts.");
        }
        return contacts;
    }
    
    /**
     * Exports a list of contacts to a specified CSV file.
     * Demonstrates basic file handling for export operations.
     *
     * @param contacts The list of contacts to export
     * @param filename The desired name/path of the export file
     * @return true if the export is successful, false otherwise
     */
    public static boolean exportContactsToCSV(List<Contact> contacts, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Type,Name,Phones,Emails"); // CSV Header
            for (Contact c : contacts) {
                String type = c.getClass().getSimpleName();
                String phones = String.join(";", c.getPhoneNumbers());
                String emails = String.join(";", c.getEmailAddresses());
                writer.println(type + "," + c.getName() + "," + phones + "," + emails);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting contacts: " + e.getMessage());
            return false;
        }
    }
}