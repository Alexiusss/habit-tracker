package com.example.habittracker.repository.inmemory;

import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.HabitStatRepository;
import com.example.habittracker.util.ValidationUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HabitRepositoryInMemory extends BaseRepositoryInMemory implements HabitRepository {
    private final Map<Integer, List<Habit>> habits = new ConcurrentHashMap<>();

    private final HabitStatRepository habitStatRepository = new HabitStatRepositoryInMemory();

    @Override
    public Habit save(Habit habit, int userId) {
        ValidationUtil.validate(habit);
        if (habit.isNew()) {
            habit.setId(globalId.incrementAndGet());
            habits.computeIfAbsent(habit.getUserId(), k -> new ArrayList<>()).add(habit);
            return habit;
        }
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

    @Override
    public boolean delete(int userId, int habitId) {
        List<Habit> userHabits = habits.get(userId);
        return userHabits.removeIf(habitStat -> habitStat.getId() == habitId) &&
                habitStatRepository.deleteAllByUserIdAndHabitId(userId, habitId);
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

    public List<Habit> getAllByUserId(Integer userId) {
        List<Habit> userHabits = this.habits.get(userId);
        return userHabits == null ? List.of() : userHabits;
    }
}