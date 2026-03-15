package com.mycontactapp.search.specification;

import com.mycontactapp.contact.Contact;

/**
 * OrSpecification
 * Concrete Specification that requires AT LEAST ONE sub-specification to match.
 *
 * @author Developer
 * @version 1.0
 */
public class OrSpecification extends Specification {
    private final Specification left;
    private final Specification right;

    /**
     * Constructs an OrSpecification linking two other specifications.
     * @param left The first specification
     * @param right The second specification
     */
    public OrSpecification(Specification left, Specification right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Evaluates if the contact satisfies AT LEAST ONE of the specifications.
     * @param contact The contact to evaluate
     * @param query The search string
     * @return true if either left or right specification matches
     */
    @Override
    public boolean matches(Contact contact, String query) {
        // Evaluate both sides using the logical OR operator (short-circuits if left is true)
        return left.matches(contact, query) || right.matches(contact, query);
    }
}
