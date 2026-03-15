package com.mycontactapp.contact;

import com.mycontactapp.tagging.Tag;
import java.util.ArrayList;
import java.util.List;

/**
 * ContactGroup
 * The Composite node in the Composite Pattern.
 * Manages a collection of ContactComponents and delegates operations to them.
 *
 * @author Developer
 * @version 1.0
 */
public class ContactGroup implements ContactComponent {
    private final String groupName;
    private final List<ContactComponent> components;

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

    @Override
    public void addTag(Tag tag) {
        for (ContactComponent component : components) {
            component.addTag(tag);
        }
    }

    @Override
    public void removeTag(Tag tag) {
        for (ContactComponent component : components) {
            component.removeTag(tag);
        }
    }

    @Override
    public void setActive(boolean isActive) {
        for (ContactComponent component : components) {
            component.setActive(isActive);
        }
    }

    @Override
    public String getFormattedDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("=== Group: ").append(groupName).append(" ===\n");
        for (ContactComponent component : components) {
            builder.append(component.getFormattedDetails()).append("\n");
        }
        builder.append("=======================\n");
        return builder.toString();
    }

    @Override
    public List<Contact> getAsContactList() {
        List<Contact> flatList = new ArrayList<>();
        for (ContactComponent component : components) {
            flatList.addAll(component.getAsContactList());
        }
        return flatList;
    }
}
