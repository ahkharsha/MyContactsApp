package com.mycontactapp.search.chain;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.search.SearchFilterInterface;

/**
 * SearchHandler
 * Abstract base class for the Chain of Responsibility pattern.
 * Each handler attempts to match the query. If it fails, it passes the 
 * request to the next handler in the chain.
 *
 * @author Developer
 * @version 1.0
 */
public abstract class SearchHandler implements SearchFilterInterface {
    // Reference to the next handler in the pipeline
    protected SearchHandler next;

    /**
     * Sets the next handler in the chain.
     * @param next The handler to link
     * @return The next handler (allows for fluent chaining)
     */
    public SearchHandler setNext(SearchHandler next) {
        this.next = next;
        return next;
    }

    /**
     * Handles the search request. If this node matches, returns true.
     * Otherwise, delegates to the next node if it exists.
     */
    @Override
    public boolean matches(Contact contact, String query) {
        if (canHandle(contact, query)) {
            return true;
        } else if (next != null) {
            return next.matches(contact, query);
        }
        return false; // Reached end of chain without match
    }

    /**
     * Determines if THIS specific node can find a match.
     * Must be implemented by concrete handlers.
     */
    protected abstract boolean canHandle(Contact contact, String query);
}
