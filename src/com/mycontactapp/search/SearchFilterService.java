package com.mycontactapp.search;

import com.mycontactapp.contact.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchFilterService
 * Handles the logic for searching contacts using various criteria.
 * Demonstrates standard for-loops and String comparison methods.
 *
 * @author Developer
 * @version 2.0
 */
public class SearchFilterService {

    /**
     * Searches for contacts that match the given query using the specified criteria.
     * @param contacts The list of contacts to search through
     * @param query The search string
     * @param criteria The strategy used to match contacts (e.g., name, phone)
     * @return A list of matching contacts
     */
    public List<Contact> search(List<Contact> contacts, String query, SearchFilterInterface criteria) {
        List<Contact> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        for (Contact contact : contacts) {
            if (criteria.matches(contact, query.trim())) {
                results.add(contact);
            }
        }
        return results;
    }

    /**
     * Strategy to search contacts by name (case-insensitive partial match).
     */
    public static class NameSearch implements SearchFilterInterface {
        /**
         * Checks if the contact's name contains the query string.
         * @param contact The contact to check
         * @param query The name fragment to search for
         * @return true if found, false otherwise
         */
        @Override
        public boolean matches(Contact contact, String query) {
            return contact.getName().toLowerCase().contains(query.toLowerCase());
        }
    }

    /**
     * Strategy to search contacts by phone number (partial match).
     */
    public static class PhoneSearch implements SearchFilterInterface {
        /**
         * Checks if any of the contact's phone numbers contain the query string.
         * @param contact The contact to check
         * @param query The phone number fragment to search for
         * @return true if found, false otherwise
         */
        @Override
        public boolean matches(Contact contact, String query) {
            for (String phone : contact.getPhoneNumbers()) {
                if (phone.contains(query)) return true;
            }
            return false;
        }
    }

    /**
     * Strategy to search contacts by email address (case-insensitive partial match).
     */
    public static class EmailSearch implements SearchFilterInterface {
        /**
         * Checks if any of the contact's email addresses contain the query string.
         * @param contact The contact to check
         * @param query The email fragment to search for
         * @return true if found, false otherwise
         */
        @Override
        public boolean matches(Contact contact, String query) {
            for (String email : contact.getEmailAddresses()) {
                if (email.toLowerCase().contains(query.toLowerCase())) return true;
            }
            return false;
        }
    }

    // Search specifically by matching Tag objects inside the Set
    public static class TagSearch implements SearchFilterInterface {
        /**
         * Checks if the contact has a tag matching the query exact name.
         * @param contact The contact to check
         * @param query   The tag name to search for
         * @return true if found, false otherwise
         */
        @Override
        public boolean matches(Contact contact, String query) {
            try {
                com.mycontactapp.tagging.Tag searchTag = new com.mycontactapp.tagging.Tag(query);
                return contact.getTags().contains(searchTag);
            } catch (IllegalArgumentException e) {
                return false; 
            }
        }
    }
}