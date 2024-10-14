package com.example.habittracker.service;

import com.example.habittracker.dto.HabitStatTo;
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

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@AllArgsConstructor
public class HabitStatService {

    private final HabitRepository habitRepository;
    private final HabitStatRepository habitStatRepository;

    public boolean markAsCompleted(Integer userid, Integer habitId) {
        return habitStatRepository.save(new HabitStat(userid, habitId)) != null;
    }

    public void deleteAllByUserIdAndHabitId(Integer userId, Integer habitId) {
        habitStatRepository.deleteAllByUserIdAndHabitId(userId, habitId);
    }

    public int getCountOfSuccessfulStreaks(Integer userId) {
        List<Habit> habits = habitRepository.getAllByUserId(userId);
        List<HabitStat> habitStats = habitStatRepository.getAllByUserId(userId);
        int counter = 0;

        for (Habit habit : habits) {
            counter += calculateStreaksCountByHabit(habit, habitStats);
        }
        return counter;
    }

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