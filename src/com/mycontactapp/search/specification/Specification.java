package com.mycontactapp.search.specification;

import com.mycontactapp.search.SearchFilterInterface;

/**
 * Specification
 * Abstract base class for the Specification Pattern, extending the base search interface.
 * Provides logical AND/OR combinators.
 *
 * @author Developer
 * @version 1.0
 */
public abstract class Specification implements SearchFilterInterface {
    
    /**
     * Chains this specification with another using a logical AND.
     * @param other The specification to combine with
     * @return A new AndSpecification
     */
    public Specification and(Specification other) {
        return new AndSpecification(this, other);
    }
    
    /**
     * Chains this specification with another using a logical OR.
     * @param other The specification to combine with
     * @return A new OrSpecification
     */
    public Specification or(Specification other) {
        return new OrSpecification(this, other);
    }
}
