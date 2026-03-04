package com.mycontactapp.search;

import com.mycontactapp.contact.Contact;

/**
 * SearchFilterInterface
 * Defines a simple contract for evaluating if a contact matches a specific search query.
 *
 * @author Developer
 * @version 1.0
 */
public interface SearchFilterInterface {
    
    /**
     * Checks if the given contact matches the search query.
     * @param contact The contact to evaluate
     * @param query The search string provided by the user
     * @return true if it matches, false otherwise
     */
    boolean matches(Contact contact, String query);
}