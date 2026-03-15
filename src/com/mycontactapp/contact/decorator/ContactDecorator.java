package com.mycontactapp.contact.decorator;

/**
 * ContactDecorator
 * Abstract base class for decorating a ContactDisplay.
 * Wraps an underlying ContactDisplay instance (either a true Contact or another decorator).
 */
public abstract class ContactDecorator implements ContactDisplay {
    private final ContactDisplay wrappee;

    public ContactDecorator(ContactDisplay wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public String getFormattedDetails() {
        return wrappee.getFormattedDetails();
    }
}
