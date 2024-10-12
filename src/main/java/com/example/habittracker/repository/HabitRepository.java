package com.example.habittracker.repository;

import com.example.habittracker.model.Habit;

import java.util.List;

public interface HabitRepository {
    Habit save(Habit habit, int userId);

    boolean delete(int id, int userId);

    Habit get(int id, int userId);

    List<Habit> getAll();
}