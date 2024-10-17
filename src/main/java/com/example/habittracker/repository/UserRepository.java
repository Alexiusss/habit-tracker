package com.example.habittracker.repository;

import com.example.habittracker.model.User;

import java.util.List;

/**
 * Repository interface for managing {@code User} entities.
 * This interface defines the basic CRUD operations for handling user data.
 *
 * @author Alexey Boyarinov
 */
public interface UserRepository {

    /**
     * Saves the given user to the repository.
     * If the user is new, it will be assigned a unique ID.
     * If the user already exists, it will be updated.
     *
     * @param user the user to save or update
     * @return the saved or updated user
     */
    User save(User user);

    /**
     * Deletes a user with the specified ID from the repository.
     *
     * @param id the ID of the user to delete
     * @return {@code true} if the user was successfully deleted, {@code false} otherwise
     */
    boolean delete(int id);

    /**
     * Retrieves a user with the specified ID from the repository.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID, or {@code null} if not found
     */
    User get(int id);

    /**
     * Retrieves a user by their email address from the repository.
     *
     * @param email the email address of the user to retrieve
     * @return the user with the specified email, or {@code null} if not found
     */
    User getByEmail(String email);

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users in the repository
     */
    List<User> getAll();
}