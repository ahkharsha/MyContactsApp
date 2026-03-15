package com.mycontactapp.search;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.search.chain.SearchHandler;
import com.mycontactapp.search.specification.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchFilterService
 * Handle logic for searching contacts using various criteria.
 * Demonstrates Specification Pattern and Chain of Responsibility.
 *
 * @author Developer
 * @version 3.0
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
     * Strategy/Specification to search contacts by name.
     */
    public static class NameSearch extends Specification {
        @Override
        public boolean matches(Contact contact, String query) {
            return contact.getName().toLowerCase().contains(query.toLowerCase());
        }
    }

    /**
     * Strategy/Specification to search contacts by phone number.
     */
    public static class PhoneSearch extends Specification {
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
     * Strategy/Specification to search contacts by email address.
     */
    public static class EmailSearch extends Specification {
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
    public static class TagSearch extends Specification {
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

    // --- Chain of Responsibility Handlers --- //

    /**
     * Chain node that attempts to match a contact by its name.
     */
    public static class NameMatchHandler extends SearchHandler {
        @Override
        protected boolean canHandle(Contact contact, String query) {
            return contact.getName().toLowerCase().contains(query.toLowerCase());
        }
    }

    /**
     * Chain node that attempts to match a contact by any of its phone numbers.
     */
    public static class PhoneMatchHandler extends SearchHandler {
        @Override
        protected boolean canHandle(Contact contact, String query) {
            for (String phone : contact.getPhoneNumbers()) {
                if (phone.contains(query)) return true;
            }
            return false;
        }
    }

    /**
     * Chain node that attempts to match a contact by any of its email addresses.
     */
    public static class EmailMatchHandler extends SearchHandler {
        @Override
        protected boolean canHandle(Contact contact, String query) {
            for (String email : contact.getEmailAddresses()) {
                if (email.toLowerCase().contains(query.toLowerCase())) return true;
            }
            return false;
        }
    }
}