package com.example.habittracker.repository.inmemory;

import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.util.ValidationUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HabitRepositoryInMemory extends BaseRepositoryInMemory implements HabitRepository {
    private final Map<Integer, List<Habit>> habits = new ConcurrentHashMap<>();

    @Override
    public Habit save(Habit habit, int userId) {
        ValidationUtil.validate(habit);
        if (habit.isNew()) {
            habit.setId(globalId.incrementAndGet());
        }
        habits.computeIfAbsent(habit.getUserId(), k -> new ArrayList<>()).add(habit);
        return habit;
    }

    @Override
    public boolean delete(int id, int userId) {
        List<Habit> userHabits = habits.get(userId);
        return userHabits.removeIf(habitStat -> habitStat.getId() == id);
    }

    @Override
    public Habit get(int id, int userId) {
        return habits.get(userId).stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(null);
    }

    @Override
    public List<Habit> getAll() {
        return habits.values().stream()
                .flatMap(Collection::stream)
                .toList();
    }
}