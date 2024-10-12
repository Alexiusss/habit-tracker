package com.example.habittracker.service;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.util.HabitUtil;
import lombok.AllArgsConstructor;

import java.util.List;

import static com.example.habittracker.util.HabitUtil.*;
import static com.example.habittracker.util.ValidationUtil.assertNotNull;
import static com.example.habittracker.util.ValidationUtil.checkNotFoundWithId;


@AllArgsConstructor
public class HabitService implements IHabitService {

    private final HabitRepository repository;

    @Override
    public HabitTo get(int id, int userId) {
        Habit habit = checkNotFoundWithId(repository.get(id, userId), id);
        return asTo(habit);
    }

    @Override
    public List<HabitTo> getAll() {
        return repository.getAll().stream()
                .map(HabitUtil::asTo)
                .toList();
    }

    @Override
    public HabitTo create(HabitTo  habitTO, int userId) {
        assertNotNull( habitTO, "Habit must not be null");
        Habit habit = createNewFromTo(habitTO);
        Habit savedHabit = repository.save(habit, userId);
        return asTo(savedHabit);
    }

    @Override
    public void update(HabitTo  habitTo, int userId) {
        assertNotNull( habitTo, "Habit must not be null");
        Habit habit = repository.get(habitTo.id(), userId);
        Habit updateHabit = updateFromTo(habit, habitTo);
        repository.save(updateHabit, userId);
    }

    @Override
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }
}