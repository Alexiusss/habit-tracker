package com.example.habittracker.repository.inmemory;

import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.util.ValidationUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HabitRepositoryInMemory extends BaseRepositoryInMemory implements HabitRepository {
    private final Map<Integer, Habit> habits = new ConcurrentHashMap<>();

    @Override
    public Habit save(Habit habit) {
        ValidationUtil.validate(habit);
        if (habit.isNew()) {
            habit.setId(globalId.incrementAndGet());
            habits.put(habit.getId(), habit);
            return habit;
        } else {
            return habits.computeIfPresent(habit.getId(), (id, oldHabit) -> habit);
        }
    }

    @Override
    public boolean delete(int id) {
        return habits.remove(id) != null;
    }

    @Override
    public Habit get(int id) {
        return habits.get(id);
    }

    @Override
    public List<Habit> getAll() {
        return habits.values().stream()
                .toList();
    }
}