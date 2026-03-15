package com.mycontactapp.filter;

import com.mycontactapp.contact.Contact;
import java.util.List;

/**
 * ContactFilter
 * Defines the Strategy interface for different filter algorithms.
 *
 * @author Developer
 * @version 1.0
 */
public interface ContactFilter {
    /**
     * Filters a given list of contacts.
     * @param contacts the original list of contacts
     * @return a new list containing only the contacts that pass the filter
     */
    List<Contact> filter(List<Contact> contacts);
}
