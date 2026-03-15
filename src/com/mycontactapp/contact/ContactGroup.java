package com.mycontactapp.contact;

import com.mycontactapp.tagging.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * ContactGroup
 * Implements the Composite Pattern to represent a structural group of ContactComponents.
 * This class allows the system to treat a single contact and a group of contacts
 * identically, enabling bulk operations (like bulk tagging or deletion) effortlessly.
 *
 * @author Developer
 * @version 1.0
 */
public class ContactGroup implements ContactComponent {
    // Name parameter for the group (e.g., "Export Group", "Bulk Delete Target")
    private String groupName;
    
    // The collection of child components (Can hold single Contacts or nested ContactGroups)
    private List<ContactComponent> components;

    public ContactGroup(String groupName) {
        this.groupName = groupName;
        this.components = new ArrayList<>();
    }

    public void add(ContactComponent component) {
        components.add(component);
    }

    public void remove(ContactComponent component) {
        components.remove(component);
    }

    @Override
    public String getName() {
        return groupName;
    }

    /**
     * Adds a tag to every child component within this group.
     * This is an example of delegating operations down the Composite tree.
     * @param tag The tag to apply
     */
    @Override
    public void addTag(Tag tag) {
        for (ContactComponent component : components) {
            component.addTag(tag);
        }
    }

    /**
     * Removes a tag from every child component within this group.
     * @param tag The tag to remove
     */
    @Override
    public void removeTag(Tag tag) {
        for (ContactComponent component : components) {
            component.removeTag(tag);
        }
    }

    /**
     * Sets the active status (soft delete) of all children within this group.
     * @param active The status to set (false to soft-delete)
     */
    @Override
    public void setActive(boolean active) {
        for (ContactComponent component : components) {
            component.setActive(active);
        }
    }

    /**
     * Gathers and returns formatted details from all children conceptually.
     * Note: Typically a group might header its name, then iterate children formats.
     * @return Formatted string overview of the group
     */
    @Override
    public String getFormattedDetails() {
        StringBuilder sb = new StringBuilder("Group: " + groupName + "\n");
        for (ContactComponent component : components) {
            sb.append(component.getFormattedDetails()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Flattens the entire composite tree into a single list of concrete Contacts.
     * This ensures groups can be easily exported or passed to legacy methods
     * requiring standard Lists.
     * @return A flattened list of all underlying Contact objects.
     */
    @Override
    public List<Contact> getAsContactList() {
        List<Contact> flatList = new ArrayList<>();
        for (ContactComponent component : components) {
            flatList.addAll(component.getAsContactList());
        }
        return flatList;
    }
}
