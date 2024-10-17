package com.example.habittracker.dto;

import java.time.Period;

/**
 * A data transfer object (DTO) representing a habit.
 *
 * @author Alexey Boyarinov
 */
public record HabitTo(
        Integer id,
        String name,
        Period frequency,
        boolean isActive,
        Integer userId
) {
}