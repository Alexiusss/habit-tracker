package com.example.habittracker.service;

import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;

import java.sql.SQLException;
import java.util.List;

/**
 * Service interface for managing users.
 * Provides CRUD methods.
 *
 * @author Alexey Boyarinov
 */
public interface UserService {

    /**
     * Retrieves a user by its ID.
     *
     * @param id the user ID
     * @return the {@code UserResponseTo} representing the user
     */
    UserResponseTo get(int id);

    /**
     * Retrieves a user by its email.
     *
     * @param email the user's email
     * @return the {@code UserResponseTo} representing the user
     */
    UserResponseTo getByEmail(String email);

    /**
     * Retrieves all users.
     *
     * @return a list of {@code UserResponseTo} objects representing all users
     */
    List<UserResponseTo> getAll();

    /**
     * Creates a new user from a {@code UserRequestTo}.
     *
     * @param user the {@code UserRequestTo} containing user details
     * @return the {@code UserResponseTo} representing the created user
     */
    UserResponseTo create(UserRequestTo user) throws SQLException;

    /**
     * Updates an existing user with the details from a {@code UserRequestTo}.
     *
     * @param user the {@code UserRequestTo} containing updated user details
     */
    void update(UserRequestTo user) throws SQLException;

    /**
     * Deletes a user by its ID.
     *
     * @param id the user ID
     */
    void delete(int id) throws SQLException;

    /**
     * Activate or block a user account.
     *
     * @param userId the user ID
     * @param enabled {@code true} to enable the user, {@code false} to disable
     */
    void enable(int userId, boolean enabled) throws SQLException;
}