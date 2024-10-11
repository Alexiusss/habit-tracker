package com.example.habittracker.repository;

import com.example.habittracker.model.Habit;

import java.util.List;

public interface HabitRepository {
    Habit save(Habit habit);

    boolean delete(int id);

    Habit get(int id);

    List<Habit> getAll();
}