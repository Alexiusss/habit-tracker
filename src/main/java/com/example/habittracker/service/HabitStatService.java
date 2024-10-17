package com.example.habittracker.service;

import com.example.habittracker.dto.HabitStatTo;

import java.time.Period;
import java.util.List;

public interface HabitStatService {
    boolean markAsCompleted(Integer userId, Integer habitId);

    void deleteAllByUserIdAndHabitId(Integer userId, int habitId);

    int getCountOfSuccessfulStreaks(Integer userId);

    List<HabitStatTo> getPercentOfSuccessfulExecution(Integer userId, Period period);
}
