package com.example.habittracker.service;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.exception.NotFoundException;

import java.util.List;

/**
 * Service interface for managing habits.
 * Provides methods for retrieving, creating, updating, and deleting habit data.
 *
 * @author Alexey Boyarinov
 */
public interface IHabitService {

    /**
     * Retrieves a habit by its ID and associated user ID.
     *
     * @param id the ID of the habit
     * @param userId the ID of the user who owns the habit
     * @return the {@code HabitTo} object corresponding to the habit
     */
    HabitTo get(int id, int userId);

    /**
     * Retrieves all habits.
     *
     * @return a list of {@code HabitTo} objects representing all habits
     */
    List<HabitTo> getAll();

    /**
     * Creates a new habit for the specified user.
     *
     * @param habit the {@code HabitTo} object representing the habit to create
     * @param userId the ID of the user for whom the habit is being created
     * @return the created {@code HabitTo} object
     */
    HabitTo create(HabitTo habit, int userId);

    /**
     * Updates an existing habit for the specified user.
     *
     * @param habit the {@code HabitTo} object representing the updated habit data
     * @param userId the ID of the user who owns the habit
     */
    void update(HabitTo habit, int userId);

    /**
     * Deletes a habit by its ID and associated user ID.
     *
     * @param id the ID of the habit
     * @param userId the ID of the user who owns the habit
     */
    void delete(int id, int userId);
}