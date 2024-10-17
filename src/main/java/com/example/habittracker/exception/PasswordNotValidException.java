package com.example.habittracker.exception;


/**
 * Exception thrown when a password is considered invalid.
 * This exception extends {@code RuntimeException}, making it an unchecked exception.
 *
 * <p>This could be used when validating user input to ensure that the password format
 * meets the expected standards</p>
 *
 * @author Alexey Boyarinov
 * @see RuntimeException
 */
public class PasswordNotValidException extends RuntimeException{
    /**
     * Constructs a new {@code PasswordNotValidException} with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public PasswordNotValidException(String message) {
        super(message);
    }
}