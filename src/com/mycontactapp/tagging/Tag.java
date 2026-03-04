package com.mycontactapp.tagging;

import java.util.Objects;

/**
 * Tag
 * Represents a custom label that can be applied to contacts for organization.
 *
 * @author Developer
 * @version 1.0
 */
public class Tag {
    private final String name;

    /**
     * Constructs a new Tag.
     * @param name The name of the tag (e.g., "Family", "Work")
     * @throws IllegalArgumentException if the name is null or empty
     */
    public Tag(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag name cannot be empty.");
        }
        this.name = name.trim().toLowerCase();
    }

    /**
     * Gets the name of the tag.
     * @return The tag name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this tag is equal to another object.
     * Tags are considered equal if they have the same name (case-insensitive).
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return name.equals(tag.name);
    }

    /**
     * Generates a hash code for the tag based on its name.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns a string representation of the tag.
     *
     * @return The tag name prefixed with '#'
     */
    @Override
    public String toString() {
        return "#" + name;
    }
}