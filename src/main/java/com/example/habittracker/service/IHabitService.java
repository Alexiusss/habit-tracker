package com.example.habittracker.service;

import com.example.habittracker.dto.HabitTo;

import java.util.List;

public interface IHabitService {
    HabitTo get(int id);

    List<HabitTo> getAll();

    HabitTo create(HabitTo habit);

    void update(HabitTo habit);

    void delete(int id);
}