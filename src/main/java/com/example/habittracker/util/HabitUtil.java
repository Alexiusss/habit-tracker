package com.example.habittracker.util;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.model.Habit;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class HabitUtil {

    public static HabitTo asTo(Habit habit) {
        return new HabitTo(habit.getId(), habit.getName(), habit.getFrequency(), habit.isActive(), habit.getUserId());
    }

    public static Habit createNewFromTo(HabitTo habitTo) {
        return Habit.builder()
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .version(0)
                .name(habitTo.name())
                .frequency(habitTo.frequency())
                .isActive(true)
                .userId(habitTo.userId())
                .build();
    }

    public static Habit updateFromTo(Habit habit, HabitTo habitTo) {
        habit.setModifiedAt(LocalDateTime.now());
        habit.setVersion(habit.getVersion() + 1);
        habit.setName(habitTo.name());
        habit.setFrequency(habitTo.frequency());
        habit.setActive(habitTo.isActive());
        return habit;
    }
}