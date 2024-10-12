package com.example.habittracker.repository;

import com.example.habittracker.model.HabitStat;

public interface HabitStatRepository {
    HabitStat save(HabitStat habitStat);
    void deleteAllByUserIdAndHabitId(Integer userId, Integer habitId);
}