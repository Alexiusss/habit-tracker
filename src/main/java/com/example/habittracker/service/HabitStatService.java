package com.example.habittracker.service;

import com.example.habittracker.model.HabitStat;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.HabitStatRepository;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
public class HabitStatService {

    private final HabitRepository habitRepository;
    private final HabitStatRepository habitStatRepository;

    public void markAsCompleted(Integer userid, Integer habitId) {
        habitStatRepository.save(new HabitStat(userid, habitId));
    }

    public void deleteAllByUserIdAndHabitId(Integer userId, Integer habitId) {
        habitStatRepository.deleteAllByUserIdAndHabitId(userId, habitId);
    }

    public void generateStatisticsOfPerformanceByPeriod(Integer userId, Integer habitId, Period period) {

    }

    public int getCountOfSuccessfulStreaks(Integer userId) {
        return 0;
    }

    public int getPercentOfSuccessfulExecutionOfHabits(Integer userId, LocalDate start, LocalDate end) {
        return 0;
    }

    public void generateProgressReport() {

    }
}