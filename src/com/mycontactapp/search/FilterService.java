package com.mycontactapp.search;

import com.mycontactapp.contact.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * FilterService
 * Handles the logic for sorting and filtering contacts.
 * Demonstrates the use of Collections.sort() and the Comparator interface.
 *
 * @author Developer
 * @version 2.0
 */
public class FilterService {

    /**
     * Sorts a list of contacts alphabetically by their name.
     * @param contacts The original list of contacts
     * @return A newly sorted list
     */
    public List<Contact> sortAlphabetically(List<Contact> contacts) {
        List<Contact> sortedList = new ArrayList<>(contacts); // Defensive copy
        
        Collections.sort(sortedList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
        
        return sortedList;
    }

    /**
     * Sorts a list of contacts by the date they were added.
     * Demonstrates conditional sorting logic.
     * @param contacts The original list of contacts
     * @param ascending true for Oldest to Newest, false for Newest to Oldest
     * @return A newly sorted list
     */
    public List<Contact> sortByDateAdded(List<Contact> contacts, boolean ascending) {
        List<Contact> sortedList = new ArrayList<>(contacts); // Defensive copy
        
        Collections.sort(sortedList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                if (ascending) {
                    return c1.getCreatedAt().compareTo(c2.getCreatedAt());
                } else {
                    return c2.getCreatedAt().compareTo(c1.getCreatedAt());
                }
            }
        });
        
        return sortedList;
    }

    /**
     * Sorts a list of contacts by view count (Frequently Contacted).
     * @param contacts The original list of contacts
     * @return A newly sorted list
     */
    public List<Contact> sortByFrequentlyContacted(List<Contact> contacts) {
        List<Contact> sortedList = new ArrayList<>(contacts);
        
        Collections.sort(sortedList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                // sort descending by viewCount
                return Integer.compare(c2.getViewCount(), c1.getViewCount());
            }
        });
        
        return sortedList;
    }
}
