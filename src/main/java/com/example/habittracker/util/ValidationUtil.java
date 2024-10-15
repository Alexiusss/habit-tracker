package com.example.habittracker.util;

import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.exception.EmailNotValidException;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.model.User;

import java.util.regex.Pattern;

/**
 * Utility class for validating {@code User} and {@code Habit} entities.
 * Provides validation for email, password, and general object checks.
 *
 * @author Alexey Boyarinov
 */
public class ValidationUtil {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    /**
     * Asserts that the given object is not null.
     *
     * @param o the object to check
     * @param message the message to display if the object is null
     * @throws NullPointerException if the object is null
     */
    public static void assertNotNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * Checks whether the given object is not null and throws a {@code NotFoundException} if it is null.
     * Used to ensure that the entity is found when retrieving by ID.
     *
     * @param object the object to check
     * @param id the ID associated with the object
     * @param <T> the type of the object
     * @return the object if it is not null
     * @throws NotFoundException if the object is null (not found)
     */
    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    /**
     * Checks whether the entity was found using its ID.
     * Throws a {@code NotFoundException} if the entity is not found.
     *
     * @param found {@code true} if the entity was found, {@code false} otherwise
     * @param id the ID of the entity
     * @throws NotFoundException if the entity was not found
     */
    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    /**
     * Checks whether the given object is not null and throws a {@code NotFoundException} if it is null.
     * Used to ensure that an entity is found using an identifier other than ID (e.g., email).
     *
     * @param object the object to check
     * @param msg the message to include in the exception if the object is null
     * @param <T> the type of the object
     * @return the object if it is not null
     * @throws NotFoundException if the object is null (not found)
     */
    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }
    /**
     * Checks whether an entity was found and throws a {@code NotFoundException} if it was not.
     *
     * @param found {@code true} if the entity was found, {@code false} otherwise
     * @param msg the message to include in the exception
     * @throws NotFoundException if the entity was not found
     */
    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    /**
     * Validates the given {@code User} by checking its email and password fields.
     * <p>
     * The email is validated using the {@link #validateEmail(CharSequence)} method to ensure it address to
     * a proper email format. The password is validated using the {@link #validatePassword(CharSequence)} method
     * to ensure it meets the required password rules (e.g., minimum length, presence of digits, etc.).
     *
     * @param user the {@code User} object to validate
     * @throws EmailNotValidException if the user's email is invalid
     * @throws PasswordNotValidException if the user's password is invalid
     * @throws NullPointerException if the email or password is {@code null} or empty
     */
    public static void validate(User user) {
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
    }

    /**
     * Validates the provided {@code Habit} entity.
     * Ensures the habit has a non-null name, frequency, and user ID.
     *
     * @param habit the {@code Habit} entity to validate
     * @throws NullPointerException if the habit's name, frequency, or user ID is null
     */
    public static void validate(Habit habit) {
        checkString(habit.getName(), "Name");
        if (habit.getFrequency() == null) {
            throw new NullPointerException("Frequency must not be null");
        }
        if(habit.getUserId() == null) {
            throw new NullPointerException("User id must not be null");
        }
    }

    /**
     * Validates the provided email.
     * Checks for non-null and valid format.
     *
     * @param email the email to validate
     * @throws EmailNotValidException if the email format is invalid
     */
    public static void validateEmail(CharSequence email) {
        checkString(email, "Email");
        boolean isEmailValid = EMAIL_REGEX.matcher(email).find();
        if (!isEmailValid) {
            throw new EmailNotValidException("Email address isn't valid");
        }
    }

    /**
     * Validates the provided password.
     * Ensures the password for non-null and meets the required format.
     *
     * @param password the password to validate
     * @throws PasswordNotValidException if the password format is invalid
     */
    public static void validatePassword(CharSequence password) {
        checkString(password, "Password");
        boolean isPasswordValid = PASSWORD_REGEX.matcher(password).find();
        if (!isPasswordValid) {
            throw new PasswordNotValidException("Password address isn't valid");
        }

    }

    /**
     * Checks if the provided {@code CharSequence} is null or empty, and throws a {@code NullPointerException}
     * with the given message if the validation fails.
     *
     * @param string the {@code CharSequence} to check
     * @param message the message to include in the exception if the check fails
     * @throws NullPointerException if the string is null or empty
     */
    private static void checkString(CharSequence string, String message) {
        if (string == null || string.isEmpty()) {
            throw new NullPointerException(message + " must not be empty");
        }
    }
}