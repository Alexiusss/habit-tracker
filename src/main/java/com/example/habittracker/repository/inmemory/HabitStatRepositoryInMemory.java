package com.example.habittracker.repository.inmemory;

import com.example.habittracker.model.HabitStat;
import com.example.habittracker.repository.HabitStatRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the {@code HabitStatRepository} interface.
 * This class stores {@code HabitStat} entities in a {@code ConcurrentHashMap},
 * with statistics mapped by the user ID.
 *
 * <p>It supports saving, deleting, and retrieving habit statistics for users,
 * and ensures that a habit cannot be marked as completed multiple times in a single day.</p>
 *
 *  @author Alexey Boyarinov
 */
public class HabitStatRepositoryInMemory extends BaseRepositoryInMemory implements HabitStatRepository {

    Map<Integer, List<HabitStat>> habitStats = new ConcurrentHashMap<>();

    /**
     * Saves a new habit statistic to the repository.
     * It checks if the habit has already been marked as completed for the current day
     * before saving.
     *
     * @param habitStat the habit statistic to save
     * @return the saved habit statistic
     * @throws IllegalArgumentException if the habit has already been marked as completed for today
     */
    @Override
    public HabitStat save(HabitStat habitStat) {
        if (isHabitStatAlreadyExistsForToday(habitStat)) {
            throw new IllegalArgumentException("A habit wit id  " + habitStat.getHabitId() + " already marked as completed for today.");
        }
        habitStat.setId(globalId.incrementAndGet());
        habitStat.setCreatedAt(LocalDateTime.now());
        habitStats.computeIfAbsent(habitStat.getUserId(), k -> new ArrayList<>()).add(habitStat);
        return habitStat;
    }


    /**
     * Deletes all habit statistics for a specific user and habit.
     *
     * @param userId  the ID of the user whose habit statistics should be deleted
     * @param habitId the ID of the habit whose statistics should be deleted
     * @return {@code true} if the habit statistics were successfully deleted, {@code false} otherwise
     */
    @Override
    public boolean deleteAllByUserIdAndHabitId(Integer userId, Integer habitId) {
        habitStats.computeIfPresent(userId, (k, list) -> {
            list.removeIf(habitStat -> habitStat.getHabitId().equals(habitId));
            return list.isEmpty() ? null : list;
        });
        return habitStats.get(userId) == null;
    }

    /**
     * Checks whether a habit statistic for the specified habit already exists for the current day.
     *
     * @param habitStat the habit statistic to check
     * @return {@code true} if the habit has already been marked as completed for today, {@code false} otherwise
     */
    private boolean isHabitStatAlreadyExistsForToday(HabitStat habitStat) {
        LocalDate today = LocalDate.now();
        return habitStats.getOrDefault(habitStat.getUserId(), List.of())
                .stream()
                .anyMatch(existingStat ->
                        existingStat.getHabitId().equals(habitStat.getHabitId()) &&
                                existingStat.getCreatedAt().toLocalDate().equals(today)
                );
    }

    /**
     * Retrieves all habit statistics associated with a specific user.
     *
     * @param userId the ID of the user whose habit statistics are to be retrieved
     * @return a list of habit statistics for the specified user
     */
    @Override
    public List<HabitStat> getAllByUserId(Integer userId) {
        return habitStats.getOrDefault(userId, List.of());
    }
}