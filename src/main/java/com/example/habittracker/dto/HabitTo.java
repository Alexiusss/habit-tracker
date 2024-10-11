package com.example.habittracker.dto;

import java.time.Period;

public record HabitTo(
        Integer id,
        String name,
        Period frequency,
        boolean isActive
) {
}