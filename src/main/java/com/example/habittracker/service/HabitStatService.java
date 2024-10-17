package com.example.habittracker.service;

import com.example.habittracker.dto.HabitStatTo;
import com.example.habittracker.exception.NotFoundException;

import java.time.Period;
import java.util.List;

/**
 * Service interface responsible for managing habit statistics.
 * Handles operations such as marking a habit as completed, deleting habit statistics,
 * calculating streak counts, and evaluating the percentage of successful executions for habits.
 *
 * @author Alexey Boyarinov
 */
public interface HabitStatService {

    /**
     * Marks a habit as completed for the given user.
     *
     * @param userId  the ID of the user
     * @param habitId the ID of the habit
     * @return {@code true} if the habit was successfully marked as completed, {@code false} otherwise
     */
    boolean markAsCompleted(Integer userId, Integer habitId);

    /**
     * Deletes all habit statistics for a specific user and habit.
     *
     * @param userId  the ID of the user
     * @param habitId the ID of the habit
     */
    void deleteAllByUserIdAndHabitId(Integer userId, int habitId);

    /**
     * Retrieves the total count of successful habit streaks for a specific user.
     * A streak is considered successful if the user consistently completes a habit according to its frequency.
     *
     * @param userId the ID of the user
     * @return the total count of successful habit streaks
     */
    int getCountOfSuccessfulStreaks(Integer userId);

    /**
     * Retrieves the percentage of successful habit execution over a given period for a specific user.
     *
     * @param userId the ID of the user
     * @param period the time period (in days) for which to calculate the percentage of successful executions
     * @return a list of {@code HabitStatTo} objects, each containing the habit name and the percentage of successful executions
     */
    List<HabitStatTo> getPercentOfSuccessfulExecution(Integer userId, Period period);
}
