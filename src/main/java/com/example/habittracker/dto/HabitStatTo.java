package com.example.habittracker.dto;

/**
 * A data transfer object (DTO) representing a habitstat.
 *
 * @author Alexey Boyarinov
 */
public record HabitStatTo(
        String name,
        Integer percentageOfCompletion
) {
}
