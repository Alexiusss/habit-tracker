package com.example.habittracker.repository;

import com.example.habittracker.model.HabitStat;

public interface HabitStatRepository {
    HabitStat save(HabitStat habitStat);
    boolean deleteAllByUserIdAndHabitId(Integer userId, Integer habitId);

    List<HabitStat> getAllByUserId(Integer userId);
}