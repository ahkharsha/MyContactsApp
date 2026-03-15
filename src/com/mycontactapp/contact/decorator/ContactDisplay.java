package com.mycontactapp.contact.decorator;

/**
 * ContactDisplay
 * Common interface defining the behavior for displaying contact details.
 * Implemented by both the base Contact class and its decorators.
 */
public interface ContactDisplay {
    /**
     * @return the formatted string details of a contact.
     */
    String getFormattedDetails();
}
