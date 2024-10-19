package com.example.habittracker.util;

import com.example.habittracker.model.HabitStat;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class HabitStatUtil {
    public static final String GET_ALL_STATS_BY_USER_ID_SQL = "SELECT * FROM entity.habit_stat WHERE user_id=?";
    public static final String INSERT_HABIT_STAT_SQL = "INSERT INTO entity.habit_stat (version, user_id, habit_id)  VALUES (?, ?, ?)";

    public static HabitStat extractHabitStatFromResultSet(ResultSet resultSet) throws SQLException {
        return HabitStat.builder()
                .id(resultSet.getInt("id"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .version(resultSet.getInt("version"))
                .userId(resultSet.getInt("user_id"))
                .habitId(resultSet.getInt("habit_id"))
                .build();
    }
}