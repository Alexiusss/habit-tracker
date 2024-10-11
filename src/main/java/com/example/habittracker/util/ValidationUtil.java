package com.example.habittracker.util;

import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.exception.EmailNotValidException;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.model.User;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern PASSWORD_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    public static void assertNotNull(Object o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void validate(User user) {
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
    }

    public static void validate(Habit habit) {
        checkString(habit.getName(), "Name");
        checkString(habit.getName(), "Name");
        if (habit.getFrequency() == null) {
            throw new NullPointerException("Frequency must no be null");
        }
    }

    public static void validateEmail(CharSequence email) {
        checkString(email, "Email");
        boolean isEmailValid = EMAIL_REGEX.matcher(email).find();
        if (!isEmailValid) {
            throw new EmailNotValidException("Email address isn't valid");
        }
    }

    public static void validatePassword(CharSequence password) {
        checkString(password, "Password");
        boolean isPasswordValid = PASSWORD_REGEX.matcher(password).find();
        if (!isPasswordValid) {
            throw new PasswordNotValidException("Password address isn't valid");
        }

    }

    private static void checkString(CharSequence string, String message) {
        if (string == null || string.isEmpty()) {
            throw new NullPointerException(message + " must not be empty");
        }
    }
}