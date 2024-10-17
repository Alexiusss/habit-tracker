package com.example.habittracker.exception;

/**
 * Exception thrown when an email address is considered invalid.
 * This exception extends {@code RuntimeException}, making it an unchecked exception.
 *
 * <p>This could be used when validating user input to ensure that the email format
 * address to the expected standards.</p>
 *
 * @author Alexey Boyarinov
 * @see RuntimeException
 */
public class EmailNotValidException extends RuntimeException {
    /**
     * Constructs a new {@code EmailNotValidException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public EmailNotValidException(String message) {
        super(message);
    }
}