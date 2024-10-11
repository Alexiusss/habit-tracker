package com.example.habittracker.util;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.model.Habit;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.Period;

@UtilityClass
public class HabitTestData {
    public static final Integer NOT_FOUND_ID = 1000;

    public static final Integer FIRST_HABIT_ID = 1;
    public static final String FIRST_HABIT_NAME = "Admin";
    public static final Period FIRST_HABIT_FREQUENCY = Period.ofDays(1);

    public static final Habit FIRST_HABIT = Habit.builder()
            .id(FIRST_HABIT_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .name(FIRST_HABIT_NAME)
            .frequency(FIRST_HABIT_FREQUENCY)
            .isActive(true)
            .build();

    public static final Habit SECOND_HABIT = Habit.builder()
            .id(2)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .name(FIRST_HABIT_NAME)
            .frequency(FIRST_HABIT_FREQUENCY)
            .isActive(true)
            .build();

    public static final HabitTo FIRST_HABIT_FROM_DB = new HabitTo(FIRST_HABIT_ID, FIRST_HABIT_NAME, FIRST_HABIT_FREQUENCY, true);

    public static final HabitTo NEW_HABIT = new HabitTo(null, FIRST_HABIT_NAME, FIRST_HABIT_FREQUENCY, true);

    public static final HabitTo UPDATED_HABIT = new HabitTo(SECOND_HABIT.getId(), "Updated habit", SECOND_HABIT.getFrequency(), true);

}
