package com.example.habittracker.exception;


/**
 * Exception thrown when a duplicate email is encountered during user registration or updating.
 * This exception extends {@code RuntimeException}, meaning it is an unchecked exception.
 *
 * <p>This could occur when attempting to create or update a user with an email
 * address that already exists in the system.</p>
 *
 * @author Alexey Boyarinov
 * @see RuntimeException
 */
public class DuplicateEmailException extends RuntimeException {

    /**
     * Constructs a new {@code DuplicateEmailException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public DuplicateEmailException(String message) {
        super(message);
    }
}