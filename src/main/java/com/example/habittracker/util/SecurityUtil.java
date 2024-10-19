package com.example.habittracker.util;

import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.exception.EmailNotValidException;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.model.Role;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.service.UserService;

import java.sql.SQLException;
import java.util.Set;

import static com.example.habittracker.util.UserUtil.asTo;
import static com.example.habittracker.util.ValidationUtil.validateEmail;

/**
 * Security utility class for handling user login, account deletion, and role-based authorization.
 * This utility is designed for in-memory implementations.
 *
 * @author Alexey Boyarinov
 */
public class SecurityUtil {

    private User currentUser;

    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * Constructs the SecurityUtil with a provided UserRepository and IUserService.
     * A default admin user is created upon initialization.
     *
     * @param userRepository the UserRepository used for accessing user data
     * @param userService    the IUserService for handling user-related operations
     */
    public SecurityUtil (UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
//        userRepository.save(new User("admin@gmail.com", "admin777", true, Set.of(Role.ADMIN, Role.USER)));
    }

    /**
     * Logs in a user by validating their email and password.
     *
     * @param email    the email of the user attempting to log in
     * @param password the password of the user attempting to log in
     * @return true if login is successful, otherwise throws an exception if credentials are invalid
     * @throws PasswordNotValidException if the password is incorrect
     * @throws EmailNotValidException if the email format is invalid
     * @throws NotFoundException if the user was not found
     */
    public boolean login(String email, String password) {
        validateEmail(email);
        User user = userRepository.getByEmail(email);
        if (user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        } else {
            throw new PasswordNotValidException("Password is incorrect");
        }
    }

    /**
     * Deletes the currently logged-in user's account.
     */
    public void deleteAccount() throws SQLException {
        userService.delete(currentUser.getId());
        currentUser = null;
    }

    /**
     * Checks if the currently logged-in user has an ADMIN role.
     *
     * @return true if the current user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return currentUser != null && currentUser.getRoles().contains(Role.ADMIN);
    }

    /**
     * Returns the profile of the currently logged-in user as a {@link UserResponseTo} transfer object.
     *
     * @return a {@code UserResponseTo} representing the current user's profile
     */
    public UserResponseTo getCurrentUserProfile() {
        return asTo(currentUser);
    }
}