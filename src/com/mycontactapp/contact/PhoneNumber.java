package com.mycontactapp.contact;

import java.util.Objects;

/**
 * PhoneNumber
 * Composed object representing a phone number within a Contact.
 */
public class PhoneNumber {
    private String number;

    /**
     * Constructs a new PhoneNumber object.
     * @param number The phone number string
     * @throws IllegalArgumentException if the number is null or empty
     */
    public PhoneNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }
        this.number = number.trim();
    }

    /**
     * Gets the phone number string.
     * @return The phone number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the phone number string.
     * @param number The new phone number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Returns the string representation of the phone number.
     * @return The phone number
     */
    @Override
    public String toString() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
