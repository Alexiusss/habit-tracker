package com.example.habittracker.util;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.model.Habit;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.Period;

import static com.example.habittracker.util.UserTestData.ADMIN_ID;

@UtilityClass
public class HabitTestData {
    public static final Integer NOT_FOUND_ID = 1000;

    public static final Integer FIRST_HABIT_ID = 11;
    public static final Integer SECOND_HABIT_ID = 12;
    public static final String FIRST_HABIT_NAME = "Habit1";
    public static final String SECOND_HABIT_NAME = "Habit2";
    public static final Period FIRST_HABIT_FREQUENCY = Period.ofDays(1);
    public static final Period SECOND_HABIT_FREQUENCY = Period.ofDays(1);
    public static final Integer USER_ID = 2;

    public static final Habit FIRST_HABIT = Habit.builder()
            .id(FIRST_HABIT_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .name(FIRST_HABIT_NAME)
            .frequency(FIRST_HABIT_FREQUENCY)
            .isActive(true)
            .userId(USER_ID)
            .build();

    public static final Habit SECOND_HABIT = Habit.builder()
            .id(SECOND_HABIT_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .name(SECOND_HABIT_NAME)
            .frequency(SECOND_HABIT_FREQUENCY)
            .isActive(true)
            .userId(USER_ID)
            .build();

    public static final Habit NEW_HABIT = Habit.builder()
            .version(0)
            .name("New habit")
            .frequency(Period.ofDays(7))
            .isActive(true)
            .userId(ADMIN_ID)
            .build();

    public static final Habit UPDATED_HABIT_1 = Habit.builder()
            .id(FIRST_HABIT_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .name("Updated name")
            .frequency(FIRST_HABIT_FREQUENCY)
            .isActive(true)
            .userId(USER_ID)
            .build();
    public static final HabitTo FIRST_HABIT_FROM_DB = new HabitTo(FIRST_HABIT_ID, FIRST_HABIT_NAME, FIRST_HABIT_FREQUENCY, true, USER_ID);

    public static final HabitTo NEW_HABIT_TO = new HabitTo(null, FIRST_HABIT_NAME, FIRST_HABIT_FREQUENCY, true, USER_ID);

    public static final HabitTo UPDATED_HABIT = new HabitTo(SECOND_HABIT.getId(), "Updated habit", SECOND_HABIT.getFrequency(), true, USER_ID);

}
