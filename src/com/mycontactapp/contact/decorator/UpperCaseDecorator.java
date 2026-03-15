package com.mycontactapp.contact.decorator;

/**
 * UpperCaseDecorator
 * Enhances contact display by converting all characters to UPPERCASE.
 */
public class UpperCaseDecorator extends ContactDecorator {

    public UpperCaseDecorator(ContactDisplay wrappee) {
        super(wrappee);
    }

    @Override
    public String getFormattedDetails() {
        String original = super.getFormattedDetails();
        return original != null ? original.toUpperCase() : null;
    }
}
