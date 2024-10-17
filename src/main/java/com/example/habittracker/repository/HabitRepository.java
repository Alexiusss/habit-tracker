package com.example.habittracker.repository;

import com.example.habittracker.model.Habit;

import java.util.List;

/**
 * Repository interface for managing {@code Habit} entities.
 * This interface defines the basic CRUD operations and allows
 * fetching habits associated with a specified user.
 *
 * @author Alexey Boyarinov
 */
public interface HabitRepository {

    /**
     * Saves or updates a habit for a specified user.
     * If the habit is new, it will be assigned a unique ID.
     * If the habit already exists, it will be updated.
     *
     * @param habit  the habit to save or update
     * @param userId the ID of the user to whom the habit belongs
     * @return the saved or updated habit
     */
    Habit save(Habit habit, int userId);

    /**
     * Deletes a habit by its ID for specified user.
     *
     * @param id     the ID of the habit to delete
     * @param userId the ID of the user to whom the habit belongs
     * @return {@code true} if the habit was successfully deleted, {@code false} otherwise
     */
    boolean delete(int id, int userId);

    /**
     * Retrieves a habit by its ID for a specified user.
     *
     * @param id     the ID of the habit to retrieve
     * @param userId the ID of the user to whom the habit belongs
     * @return the habit with the specified ID, or {@code null} if not found
     */
    Habit get(int id, int userId);

    /**
     * Retrieves all habits from the repository.
     *
     * @return a list of all habits in the repository
     */
    List<Habit> getAll();

    /**
     * Retrieves all habits associated with a specific user.
     *
     * @param userId the ID of the user whose habits are to be retrieved
     * @return a list of habits associated with the specified user
     */
    List<Habit> getAllByUserId(Integer userId);
}