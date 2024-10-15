package com.example.habittracker.repository;

import com.example.habittracker.model.HabitStat;

import java.util.List;

/**
 * Repository interface for managing {@code HabitStat} entities.
 * This interface defines the operations for saving, deleting, and retrieving
 * habit statistics associated with users and their habits.
 */
public interface HabitStatRepository {

    /**
     * Saves a habit statistic.
     *
     * @param habitStat the habit statistic to save
     * @return the saved habit statistic
     */
    HabitStat save(HabitStat habitStat);

    /**
     * Deletes all habit statistics for a specific user and habit.
     *
     * @param userId  the ID of the user whose habit statistics should be deleted
     * @param habitId the ID of the habit whose statistics should be deleted
     * @return {@code true} if the statistics were successfully deleted, {@code false} otherwise
     */
    boolean deleteAllByUserIdAndHabitId(Integer userId, Integer habitId);

    /**
     * Retrieves all habit statistics associated with a specific user.
     *
     * @param userId the ID of the user whose habit statistics are to be retrieved
     * @return a list of habit statistics for the specified user
     */
    List<HabitStat> getAllByUserId(Integer userId);
}