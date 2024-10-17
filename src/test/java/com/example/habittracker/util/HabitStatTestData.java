package com.example.habittracker.util;

import com.example.habittracker.model.HabitStat;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static com.example.habittracker.util.HabitTestData.FIRST_HABIT_ID;
import static com.example.habittracker.util.HabitTestData.USER_ID;

@UtilityClass
public class HabitStatTestData {
    public static final Integer FIRST_HABIT_STAT_ID = 111;

    public static HabitStat FIRST_HABIT_STAT = HabitStat.builder()
            .id(FIRST_HABIT_STAT_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .userId(USER_ID)
            .habitId(FIRST_HABIT_ID)
            .build();
}