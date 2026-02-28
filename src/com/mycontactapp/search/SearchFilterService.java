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
 * @version 1.0
 */
public class SearchFilterService {

    /**
     * General method to filter a list of contacts based on a given criteria.
     *
     * @param contacts The list of contacts to search through
     * @param query    The search string
     * @param criteria The specific search implementation to use
     * @return A list of contacts that match the query
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
     * Separate class for Name searching using contains() and toLowerCase().
     */
    public static class NameSearch implements SearchFilterInterface {
        @Override
        public boolean matches(Contact contact, String query) {
            return contact.getName().toLowerCase().contains(query.toLowerCase());
        }
    }

    /**
     * Separate class for Phone searching iterating through the phone list.
     */
    public static class PhoneSearch implements SearchFilterInterface {
        @Override
        public boolean matches(Contact contact, String query) {
            for (String phone : contact.getPhoneNumbers()) {
                if (phone.contains(query)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Separate class for Email searching using case-insensitive matching.
     */
    public static class EmailSearch implements SearchFilterInterface {
        @Override
        public boolean matches(Contact contact, String query) {
            for (String email : contact.getEmailAddresses()) {
                if (email.toLowerCase().contains(query.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
    }
}