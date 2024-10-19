package com.example.habittracker.util;

import com.example.habittracker.model.HabitStat;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static com.example.habittracker.util.HabitTestData.*;

@UtilityClass
public class HabitStatTestData {
    public static final Integer FIRST_HABIT_STAT_ID = 21;
    public static final Integer SECOND_HABIT_STAT_ID = 22;

    public static HabitStat FIRST_HABIT_STAT = HabitStat.builder()
            .id(FIRST_HABIT_STAT_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .userId(USER_ID)
            .habitId(FIRST_HABIT_ID)
            .build();

    public static HabitStat SECOND_HABIT_STAT = HabitStat.builder()
            .id(SECOND_HABIT_STAT_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .userId(USER_ID)
            .habitId(SECOND_HABIT_ID)
            .build();

    public static HabitStat NEW_HABIT_STAT = HabitStat.builder()
            .createdAt(LocalDateTime.now().minusDays(1))
            .version(0)
            .userId(USER_ID)
            .habitId(SECOND_HABIT_ID)
            .build();
}