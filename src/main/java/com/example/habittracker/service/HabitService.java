package com.example.habittracker.service;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.util.HabitUtil;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static com.example.habittracker.util.HabitUtil.*;
import static com.example.habittracker.util.ValidationUtil.assertNotNull;
import static com.example.habittracker.util.ValidationUtil.checkNotFoundWithId;

/**
 * Service implementation for managing habits.
 * Implements the {@link IHabitService} interface to handle the business logic of creating, updating,
 * retrieving, and deleting habits for users.
 *
 * @author Alexey Boyarinov
 */
@AllArgsConstructor
public class HabitService implements IHabitService {

    private final HabitRepository repository;

    /**
     * Retrieves a habit by its ID and user ID.
     *
     * @param id the ID of the habit to retrieve
     * @param userId the ID of the user who owns the habit
     * @return the corresponding {@code HabitTo} object
     * @throws NotFoundException if no habit with the specified ID exists for the user
     */
    @Override
    public HabitTo get(int id, int userId) {
        Habit habit = checkNotFoundWithId(repository.get(id, userId), id);
        return asTo(habit);
    }

    /**
     * Retrieves all habits in the system.
     *
     * @return a list of {@code HabitTo} objects representing all habits
     */
    @Override
    public List<HabitTo> getAll() {
        return repository.getAll().stream()
                .map(HabitUtil::asTo)
                .toList();
    }

    /**
     * Retrieves all habits for a particular user, with extra filtering based on a specific predicate.
     *
     * @param userId the ID of the user
     * @param predicate a predicate to filter habits
     * @return a list of {@code HabitTo} objects matching the filter
     */
    public List<HabitTo> getAllByUserId(Integer userId, Predicate<Habit> predicate) {
        return repository.getAllByUserId(userId).stream()
                .filter(predicate)
                .map(HabitUtil::asTo)
                .toList();
    }

    /**
     * Creates a new habit for a user.
     *
     * @param habitTO the {@code HabitTo} object representing the habit to create
     * @param userId the ID of the user who owns the habit
     * @return the created {@code HabitTo} object
     * @throws NullPointerException if the habit is null
     */
    @Override
    public HabitTo create(HabitTo habitTO, int userId) {
        assertNotNull(habitTO, "Habit must not be null");
        Habit habit = createNewFromTo(habitTO);
        Habit savedHabit = repository.save(habit, userId);
        return asTo(savedHabit);
    }

    /**
     * Updates an existing habit for a user.
     *
     * @param habitTo the {@code HabitTo} object with updated information
     * @param userId the ID of the user who owns the habit
     * @throws NullPointerException if the habit is null
     * @throws NotFoundException if no habit with the specified ID exists for the user
     */
    @Override
    public void update(HabitTo  habitTo, int userId) {
        assertNotNull( habitTo, "Habit must not be null");
        Habit habit = repository.get(habitTo.id(), userId);
        Habit updateHabit = updateFromTo(habit, habitTo);
        repository.save(updateHabit, userId);
    }

    /**
     * Deletes a habit by its ID and associated user ID.
     *
     * @param id the ID of the habit to delete
     * @param userId the ID of the user who owns the habit
     * @throws NotFoundException if no habit with the specified ID exists for the user
     */
    @Override
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }
}