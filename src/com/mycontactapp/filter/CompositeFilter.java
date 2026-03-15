package com.mycontactapp.filter;

import com.mycontactapp.contact.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * CompositeFilter
 * Implements the Composite Pattern to allow multiple ContactFilters to be combined.
 * It applies each filter sequentially.
 *
 * @author Developer
 * @version 1.0
 */
public class CompositeFilter implements ContactFilter {
    private final List<ContactFilter> filters = new ArrayList<>();

    /**
     * Adds a filter to the composite collection.
     * @param filter The ContactFilter to add
     */
    public void addFilter(ContactFilter filter) {
        filters.add(filter);
    }

    /**
     * Applies all filters in sequence (multi-level filtering).
     * @param contacts the original list of contacts
     * @return a new list containing only the contacts that passed ALL filters
     */
    @Override
    public List<Contact> filter(List<Contact> contacts) {
        List<Contact> filtered = new ArrayList<>(contacts);
        for (ContactFilter f : filters) {
            filtered = f.filter(filtered);
        }
        return filtered;
    }
}
