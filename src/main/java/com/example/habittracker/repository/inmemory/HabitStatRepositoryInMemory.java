package com.example.habittracker.repository.inmemory;

import com.example.habittracker.model.HabitStat;
import com.example.habittracker.repository.HabitStatRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HabitStatRepositoryInMemory extends BaseRepositoryInMemory implements HabitStatRepository {
    Map<Integer, List<HabitStat>> habitStats = new ConcurrentHashMap<>();

    @Override
    public HabitStat save(HabitStat habitStat) {
        if (isHabitStatAlreadyExistsForToday(habitStat)) {
            throw new IllegalArgumentException("A HabitStat already exists for habit id: " + habitStat.getHabitId() + " for today.");
        }
        habitStat.setId(globalId.incrementAndGet());
        habitStats.computeIfAbsent(habitStat.getUserId(), k -> new ArrayList<>()).add(habitStat);
        return habitStat;
    }

    @Override
    public void deleteAllByUserIdAndHabitId(Integer userId, Integer habitId) {
        habitStats.computeIfPresent(userId, (k, list) -> {
            list.removeIf(habitStat -> habitStat.getHabitId().equals(habitId));
            return list.isEmpty() ? null : list;
        });
    }

    private boolean isHabitStatAlreadyExistsForToday(HabitStat habitStat) {
        LocalDate today = LocalDate.now();
        return habitStats.get(habitStat.getUserId())
                .stream()
                .anyMatch(existingStat ->
                        existingStat.getHabitId().equals(habitStat.getHabitId()) &&
                                existingStat.getCreatedAt().toLocalDate().equals(today)
                );
    }
}