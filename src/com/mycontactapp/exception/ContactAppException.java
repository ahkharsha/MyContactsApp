package com.mycontactapp.exception;

/**
 * ContactAppException
 * A custom error for our app.
 *
 * @author Developer
 * @version 1.0
 */
public class ContactAppException extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructs a new ContactAppException with the specified detail message.
     * @param message The detail message explaining the reason for the exception
     */
    public ContactAppException(String message) {
        super(message);
    }
}