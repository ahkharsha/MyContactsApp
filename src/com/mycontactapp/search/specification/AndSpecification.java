package com.mycontactapp.search.specification;

import com.mycontactapp.contact.Contact;

/**
 * AndSpecification
 * Concrete Specification that requires BOTH sub-specifications to match.
 *
 * @author Developer
 * @version 1.0
 */
public class AndSpecification extends Specification {
    private final Specification left;
    private final Specification right;

    /**
     * Constructs an AndSpecification linking two other specifications.
     * @param left The first specification
     * @param right The second specification
     */
    public AndSpecification(Specification left, Specification right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Evaluates if the contact satisfies BOTH specifications.
     * @param contact The contact to evaluate
     * @param query The search string
     * @return true if both left and right specifications match
     */
    @Override
    public boolean matches(Contact contact, String query) {
        // Evaluate both sides using the logical AND operator
        return left.matches(contact, query) && right.matches(contact, query);
    }
}
