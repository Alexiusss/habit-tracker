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
 *  @author Alexey Boyarinov
 */
public class HabitStatRepositoryInMemory extends BaseRepositoryInMemory implements HabitStatRepository {

    Map<Integer, List<HabitStat>> habitStats = new ConcurrentHashMap<>();

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

    @Override
    public boolean deleteAllByUserIdAndHabitId(Integer userId, Integer habitId) {
        habitStats.computeIfPresent(userId, (k, list) -> {
            list.removeIf(habitStat -> habitStat.getHabitId().equals(habitId));
            return list.isEmpty() ? null : list;
        });
        return habitStats.get(userId) == null;
    }

    private boolean isHabitStatAlreadyExistsForToday(HabitStat habitStat) {
        LocalDate today = LocalDate.now();
        return habitStats.getOrDefault(habitStat.getUserId(), List.of())
                .stream()
                .anyMatch(existingStat ->
                        existingStat.getHabitId().equals(habitStat.getHabitId()) &&
                                existingStat.getCreatedAt().toLocalDate().equals(today)
                );
    }

    @Override
    public List<HabitStat> getAllByUserId(Integer userId) {
        return habitStats.getOrDefault(userId, List.of());
    }
}