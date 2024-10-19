package com.example.habittracker.util;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.model.Habit;
import lombok.experimental.UtilityClass;
import org.postgresql.util.PGInterval;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Utility class for converting between Habit entities and Habit transfer objects (HabitTo),
 * and for creating or updating Habit objects.
 *
 * @author Alexey Boyarinov
 */
@UtilityClass
public class HabitUtil {

    public static final String INSERT_HABIT_SQL = "INSERT INTO entity.habit (version, name, frequency, is_active, user_id) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_HABIT_SQL = "UPDATE entity.habit SET modified_at=?, version=?, name=?, frequency=?, is_active=? WHERE id=?";
    public static final String DELETE_HABIT_SQL = "DELETE entity.habit WHERE habit_id=? AND user_id=?";
    public static final String SELECT_ALL_HABITS_SQL = "SELECT * FROM entity.habit";
    public static final String SELECT_ALL_HABITS_BY_USER_SQL = "SELECT * FROM entity.habit WHERE user_id=?";
    public static final String SELECT_HABIT_BY_ID_AND_USER_SQL = "SELECT * FROM entity.habit WHERE id=? AND user_id=?";


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

    public static Habit extractUserFromResultSet(ResultSet rs) throws SQLException {
        return Habit.builder()
                .id(rs.getInt("id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .version(rs.getInt("version"))
                .name(rs.getString("name"))
                .frequency(convertPGIntervalToPeriod((PGInterval)rs.getObject("frequency")))
                .isActive(rs.getBoolean("is_active"))
                .userId(rs.getInt("user_id"))
                .build();
    }

    private static Period convertPGIntervalToPeriod(PGInterval interval) {
        return Period.ofDays(interval.getDays());
    }

    public static PGInterval convertPeriodToPGInterval(Period period) {
        int daysCount = period.getDays();
        try {
            return new PGInterval(daysCount + " day");
        } catch (SQLException e) {
            throw new RuntimeException("Exception occurs while converting Period to PGInterval: \n" + e.getMessage());
        }
    }
}