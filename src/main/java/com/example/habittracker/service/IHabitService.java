package com.example.habittracker.service;

import com.example.habittracker.dto.HabitTo;

import java.util.List;

public interface IHabitService {
    HabitTo get(int id, int userId);

    List<HabitTo> getAll();

    HabitTo create(HabitTo habit, int userId);

    void update(HabitTo habit, int userId);

    void delete(int id, int userId);
}