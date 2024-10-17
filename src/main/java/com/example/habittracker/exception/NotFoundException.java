package com.example.habittracker.exception;

/**
 * Exception thrown when a requested resource is not found.
 * This exception extends {@code RuntimeException}, making it an unchecked exception.
 *
 * <p>This can be used to signal that a specific entity (such as a user, habit, or other resource)
 * was not found in the system.</p>
 *
 * @author Alexey Boyarinov
 * @see RuntimeException
 */
public class NotFoundException extends RuntimeException{
    /**
     * Constructs a new {@code NotFoundException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public NotFoundException(String message) {
        super(message);
    }
}