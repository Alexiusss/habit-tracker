package com.example.habittracker.repository.inmemory;

import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.HabitStatRepository;
import com.example.habittracker.util.ValidationUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the {@code HabitRepository} interface.
 * This class stores {@code Habit} entities in a {@code ConcurrentHashMap},
 * with habits mapped by the user ID.
 *
 * <p>It uses an atomic counter to generate unique IDs for new habits.</p>
 *
 * @author Alexey Boyarinov
 */
public class HabitRepositoryInMemory extends BaseRepositoryInMemory implements HabitRepository {
    private final Map<Integer, List<Habit>> habits = new ConcurrentHashMap<>();

    private final HabitStatRepository habitStatRepository = new HabitStatRepositoryInMemory();

    /**
     * Saves or updates a habit for a specific user.
     * If the habit is new, a unique ID is assigned, and the habit is added to the list
     * of the specified user. If the habit exists, it is updated in the user's habit list.
     *
     * @param habit  the habit to save or update
     * @param userId the ID of the user to whom the habit belongs
     * @return the saved or updated habit
     */
    @Override
    public Habit save(Habit habit, int userId) {
        ValidationUtil.validate(habit);
        if (habit.isNew()) {
            habit.setId(globalId.incrementAndGet());
            habits.computeIfAbsent(habit.getUserId(), k -> new ArrayList<>()).add(habit);
            return habit;
        } else
            habits.computeIfPresent(habit.getUserId(), (k, list) -> {
                for (int i = 0; i < list.size(); i++) {
                    if (Objects.equals(list.get(i).getId(), habit.getId())) {
                        list.set(i, habit);
                        break;
                    }
                }
                return list;
            });
        return habit;
    }

    /**
     * Deletes a habit by its ID for a specific user.
     * It also deletes all habit statistics associated with the habit for the user.
     *
     * @param userId  the ID of the user to whom the habit belongs
     * @param habitId the ID of the habit to delete
     * @return {@code true} if the habit was successfully deleted, {@code false} otherwise
     */
    @Override
    public boolean delete(int userId, int habitId) {
        List<Habit> userHabits = habits.get(userId);
        return userHabits.removeIf(habitStat -> habitStat.getId() == habitId) &&
                habitStatRepository.deleteAllByUserIdAndHabitId(userId, habitId);
    }

    /**
     * Retrieves a habit by its ID for a specific user.
     *
     * @param id     the ID of the habit to retrieve
     * @param userId the ID of the user to whom the habit belongs
     * @return the habit with the specified ID, or {@code null} if not found
     */
    @Override
    public Habit get(int id, int userId) {
        return habits.get(userId).stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all habits stored in the repository.
     *
     * @return a list of all habits in the repository
     */
    @Override
    public List<Habit> getAll() {
        return habits.values().stream()
                .flatMap(Collection::stream)
                .toList();
    }

    /**
     * Retrieves all habits associated with a specific user.
     *
     * @param userId the ID of the user whose habits are to be retrieved
     * @return a list of habits associated with the specified user
     */
    public List<Habit> getAllByUserId(Integer userId) {
        List<Habit> userHabits = this.habits.get(userId);
        return userHabits == null ? List.of() : userHabits;
    }
}