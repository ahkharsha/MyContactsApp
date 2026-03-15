package com.mycontactapp.util;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.contact.Organization;
import com.mycontactapp.contact.Person;
import com.mycontactapp.contact.builder.ContactBuilder;
import com.mycontactapp.contact.factory.ContactFactory;
import com.mycontactapp.user.builder.UserBuilder;
import com.mycontactapp.user.factory.UserFactory;
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
 * @version 3.0
 */
public class FileHandler {

    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String CONTACTS_FILE = DATA_DIR + "/contacts.txt";
    private static final String DELIMITER = "\\|"; 
    private static final UserFactory USER_FACTORY = new UserFactory();
    private static final ContactFactory CONTACT_FACTORY = new ContactFactory();

    static {
        new File(DATA_DIR).mkdirs(); 
    }

    /**
     * Saves the list of registered users to a text file.
     * The file format includes user type, ID, email, hashed password, and full name.
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
     * Loads the list of registered users from a text file.
     * If the file does not exist, an empty list is returned.
     * @return A list of User objects (FreeUser or PremiumUser)
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
                    UserBuilder builder = new UserBuilder()
                            .setUserId(parts[1])
                            .setEmail(parts[2])
                            .setPasswordHash(parts[3])
                            .setFullName(parts[4]);
                    User user = USER_FACTORY.createUser(parts[0], builder);
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
     * Handles both Person and Organization types, including their specific fields.
     * @param contacts The list of contacts to save
     */
    public static void saveContacts(List<Contact> contacts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONTACTS_FILE))) {
            for (Contact c : contacts) {
                String type = c instanceof Person ? "Person" : "Organization";
                String phones = String.join(",", c.getPhoneNumbers());
                String emails = String.join(",", c.getEmailAddresses());
                String extra = c instanceof Person ? ((Person) c).getRelationship() : ((Organization) c).getWebsite();
                
                String tags = c.getTags().stream().map(com.mycontactapp.tagging.Tag::getName).reduce((t1, t2) -> t1 + "," + t2).orElse("");
                
                writer.println(type + "|" + c.getContactId() + "|" + c.getUserId() + "|" + c.getName() + "|" 
                        + phones + "|" + emails + "|" + c.getCreatedAt().toString() + "|" + extra + "|" + tags + "|" + c.getViewCount() + "|" + c.isActive());
            }
        } catch (IOException e) {
            System.err.println("Error saving contacts to file.");
        }
    }

    /**
     * Loads the list of contacts from a text file.
     * Reconstructs complex objects including lists of phones, emails, and tags.
     * @return A list of Contact objects
     */
    public static List<Contact> loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        File file = new File(CONTACTS_FILE);
        if (!file.exists()) return contacts;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(DELIMITER, -1);
                if (p.length >= 8) {
                    List<String> phones = p[4].isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(p[4].split(",")));
                    List<String> emails = p[5].isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(p[5].split(",")));
                    LocalDateTime createdAt = LocalDateTime.parse(p[6]);
                    
                    int viewCount = 0;
                    boolean isActive = true;
                    if (p.length >= 10 && !p[9].isEmpty()) {
                        try { viewCount = Integer.parseInt(p[9]); } catch (NumberFormatException e) {}
                    }
                    if (p.length >= 11 && !p[10].isEmpty()) {
                        isActive = Boolean.parseBoolean(p[10]);
                    }

                    ContactBuilder builder = new ContactBuilder()
                        .setContactId(p[1])
                        .setUserId(p[2])
                        .setName(p[3])
                        .setPhoneNumbers(phones)
                        .setEmailAddresses(emails)
                        .setCreatedAt(createdAt)
                        .setViewCount(viewCount)
                        .setIsActive(isActive);
                        
                    if (p[0].equals("Person")) {
                        builder.setRelationship(p[7]);
                    } else {
                        builder.setWebsite(p[7]);
                    }

                    Contact c = CONTACT_FACTORY.createContact(p[0], builder);

                    if (p.length >= 9 && !p[8].isEmpty()) {
                        for (String tagName : p[8].split(",")) {
                            c.addTag(new com.mycontactapp.tagging.Tag(tagName));
                        }
                    }
                    contacts.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading contacts.");
        }
        return contacts;
    }

    /**
     * Exports the provided list of contacts to a CSV file.
     * Useful for backing up data or transferring to another application.
     * @param contacts The list of contacts to export
     * @param filename The name of the file to save (e.g., "contacts.csv")
     * @return true if the export was successful, false otherwise
     */
    public static boolean exportContactsToCSV(List<Contact> contacts, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Type,Name,Phones,Emails");
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