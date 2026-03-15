package com.mycontactapp.filter;

import com.mycontactapp.contact.Contact;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FrequentlyContactedFilter
 * Concrete Strategy that filters contacts viewed more than a given threshold.
 *
 * @author Developer
 * @version 1.0
 */
public class FrequentlyContactedFilter implements ContactFilter {
    private final int minimumViews;

    /**
     * Constructs a filter requiring a minimum number of views.
     * @param minimumViews The threshold for view count.
     */
    public FrequentlyContactedFilter(int minimumViews) {
        this.minimumViews = minimumViews;
    }

    /**
     * @param contacts the original list of contacts
     * @return a new list containing only contacts with view count >= threshold
     */
    @Override
    public List<Contact> filter(List<Contact> contacts) {
        return contacts.stream()
                .filter(c -> c.getViewCount() >= minimumViews)
                .collect(Collectors.toList());
    }
}
