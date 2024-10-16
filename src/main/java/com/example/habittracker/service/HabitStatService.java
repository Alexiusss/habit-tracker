package com.example.habittracker.service;

import com.example.habittracker.dto.HabitStatTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.BaseEntity;
import com.example.habittracker.model.Habit;
import com.example.habittracker.model.HabitStat;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.HabitStatRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.example.habittracker.util.ValidationUtil.checkNotFoundWithId;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Service class responsible for managing habit statistics.
 * Handles operations such as marking a habit as completed, deleting habit statistics,
 * calculating streak counts, and evaluating the percentage of successful executions for habits.
 *
 * @author Alexey Boyarinov
 */
@AllArgsConstructor
public class HabitStatService {

    private final HabitRepository habitRepository;
    private final HabitStatRepository habitStatRepository;

    /**
     * Marks a habit as completed for the given user.
     *
     * @param userId  the ID of the user
     * @param habitId the ID of the habit
     * @return {@code true} if the habit was successfully marked as completed, {@code false} otherwise
     * @throws IllegalArgumentException if the habit was already marked as completed for today
     */
    public boolean markAsCompleted(Integer userId, Integer habitId) {
        return habitStatRepository.save(new HabitStat(userId, habitId)) != null;
    }

    /**
     * Deletes all habit statistics for a specific user and habit.
     *
     * @param userId  the ID of the user
     * @param habitId the ID of the habit
     * @throws NotFoundException if no habitStat with the specified ID exists for the user
     */
    public void deleteAllByUserIdAndHabitId(Integer userId, int habitId) {
        checkNotFoundWithId(habitStatRepository.deleteAllByUserIdAndHabitId(userId, habitId), habitId);
    }

    /**
     * Retrieves the total count of successful habit streaks for a specific user.
     * A streak is considered successful if the user consistently completes a habit according to its frequency.
     *
     * @param userId the ID of the user
     * @return the total count of successful habit streaks
     */
    public int getCountOfSuccessfulStreaks(Integer userId) {
        List<Habit> habits = habitRepository.getAllByUserId(userId);
        List<HabitStat> habitStats = habitStatRepository.getAllByUserId(userId);
        int counter = 0;

        for (Habit habit : habits) {
            counter += calculateStreaksCountByHabit(habit, habitStats);
        }
        return counter;
    }

    /**
     * Calculates the number of successful streaks for a specific habit based on its statistics.
     *
     * @param habit      the habit to calculate streaks for
     * @param habitStats the list of all habit statistics for the user
     * @return the count of successful streaks for the given habit
     */
    private int calculateStreaksCountByHabit(Habit habit, List<HabitStat> habitStats) {
        int counter = 0;
        int currentStreak = 0;
        int habitFrequency = habit.getFrequency().getDays();

        List<LocalDate> dateList = habitStats.stream()
                .filter(habitStat -> habitStat.getHabitId().equals(habit.getId()))
                .sorted(Comparator.comparing(BaseEntity::getCreatedAt))
                .map(h -> h.getCreatedAt().toLocalDate())
                .toList();

        if (dateList.isEmpty()) {
            return 0;
        }

        if (dateList.size() == 1 && habitFrequency == 1) {
            return 1;
        }

        for (int i = 1; i < dateList.size(); i++) {
            if (dateList.get(i).minusDays(habitFrequency).equals(dateList.get(i - 1))) {
                currentStreak++;
                if (currentStreak == habitFrequency) {
                    counter++;
                    currentStreak = 0;
                }
            } else {
                currentStreak = 0;
            }
        }
        return counter;
    }

    /**
     * Retrieves the percentage of successful habit execution over a given period for a specific user.
     *
     * @param userId the ID of the user
     * @param period the time period (in days) for which to calculate the percentage of successful executions
     * @return a list of {@code HabitStatTo} objects, each containing the habit name and the percentage of successful executions
     */
    public List<HabitStatTo> getPercentOfSuccessfulExecution(Integer userId, Period period) {
        int days = period.getDays();
        LocalDate startDate = LocalDate.now().minusDays(days);

        Map<Integer, Long> executedHabits = habitStatRepository.getAllByUserId(userId).stream()
                .filter(hs -> hs.getCreatedAt().toLocalDate().isAfter(startDate))
                .collect(groupingBy(HabitStat::getHabitId, counting()));

        return habitRepository.getAllByUserId(userId).stream()
                .map(habit -> {
                    long executedCount = executedHabits.getOrDefault(habit.getId(), 0L);
                    int percentage = (int) ((double) executedCount / days * 100);
                    return new HabitStatTo(habit.getName(), percentage);
                })
                .toList();
    }
}