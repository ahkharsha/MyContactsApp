package com.mycontactapp.filter;

import com.mycontactapp.contact.Contact;
import com.mycontactapp.tagging.Tag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TagFilter
 * Concrete Strategy that filters contacts having a specific Tag.
 *
 * @author Developer
 * @version 1.0
 */
public class TagFilter implements ContactFilter {
    private final Tag targetTag;

    /**
     * Constructs a TagFilter for the specified tag name.
     * @param tagName The tag to filter by
     */
    public TagFilter(String tagName) {
        this.targetTag = new Tag(tagName);
    }

    /**
     * @param contacts the original list of contacts
     * @return a new list containing only contacts that have the target tag
     */
    @Override
    public List<Contact> filter(List<Contact> contacts) {
        return contacts.stream()
                .filter(c -> c.getTags().contains(targetTag))
                .collect(Collectors.toList());
    }
}
