package com.example.habittracker.util;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.model.Habit;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

/**
 * Utility class for converting between Habit entities and Habit transfer objects (HabitTo),
 * and for creating or updating Habit objects.
 *
 * @author Alexey Boyarinov
 */
@UtilityClass
public class HabitUtil {

    /**
     * Converts a {@link Habit} entity to a {@link HabitTo} transfer object.
     *
     * @param habit the Habit entity to convert
     * @return a new {@code HabitTo} containing the data from the given Habit entity
     */
    public static HabitTo asTo(Habit habit) {
        return new HabitTo(habit.getId(), habit.getName(), habit.getFrequency(), habit.isActive(), habit.getUserId());
    }

    /**
     * Creates a new {@link Habit} entity from a {@link HabitTo} transfer object.
     * Sets default values for creation time and initial version.
     *
     * @param habitTo the Habit transfer object to convert
     * @return a new {@code Habit} entity populated with data from the given transfer object
     */
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

    /**
     * Updates an existing {@link Habit} entity using the data from a {@link HabitTo} transfer object.
     * Updates the modified time and increments the version of the habit.
     *
     * @param habit   the existing Habit entity to update
     * @param habitTo the Habit transfer object containing the updated data
     * @return the updated Habit entity
     */
    public static Habit updateFromTo(Habit habit, HabitTo habitTo) {
        habit.setModifiedAt(LocalDateTime.now());
        habit.setVersion(habit.getVersion() + 1);
        habit.setName(habitTo.name());
        habit.setFrequency(habitTo.frequency());
        habit.setActive(habitTo.isActive());
        return habit;
    }
}