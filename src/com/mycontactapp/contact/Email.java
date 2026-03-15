package com.mycontactapp.contact;

import java.util.Objects;

/**
 * Email
 * Composed object representing an email address within a Contact.
 */
public class Email {
    private String address;

    /**
     * Constructs a new Email object.
     * @param address The email address string
     * @throws IllegalArgumentException if the address is null or empty
     */
    public Email(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Email address cannot be empty.");
        }
        this.address = address.trim();
    }

    /**
     * Gets the email address string.
     * @return The email address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the email address string.
     * @param address The new email address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the string representation of the email address.
     * @return The email address
     */
    @Override
    public String toString() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
