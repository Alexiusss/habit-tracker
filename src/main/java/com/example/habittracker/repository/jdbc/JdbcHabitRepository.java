package com.example.habittracker.repository.jdbc;

import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.habittracker.util.HabitUtil.*;

@AllArgsConstructor
public class JdbcHabitRepository implements HabitRepository {

    DataSource dataSource;

    @Override
    public Habit save(Habit habit, int userId) {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            if (habit.isNew()) {
                PreparedStatement ps = connection.prepareStatement(INSERT_HABIT_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, habit.getVersion());
                ps.setString(2, habit.getName());
                ps.setObject(3, convertPeriodToPGInterval(habit.getFrequency()));
                ps.setBoolean(4, habit.isActive());
                ps.setInt(5, habit.getUserId());
                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt("id");
                    habit.setId(id);
                }
                connection.commit();
                connection.close();
                return habit;
            } else {
                PreparedStatement ps = connection.prepareStatement(UPDATE_HABIT_SQL);
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(2, habit.getVersion());
                ps.setString(3, habit.getName());
                ps.setObject(4, convertPeriodToPGInterval(habit.getFrequency()));
                ps.setBoolean(5, habit.isActive());
                ps.setInt(6, habit.getId());
                ps.executeUpdate();
                connection.commit();
                return null;
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("Exception occurred while saving to the JDBC habit repository: \n" + e.getMessage());
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Connection connection = getConnection();
        int deletedCount;
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(DELETE_HABIT_SQL);
            ps.setInt(1, id);
            ps.setInt(2, userId);
            deletedCount = ps.executeUpdate();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException("Exception while deleting habit with id" + id + ":\n" + e.getMessage());
        }
        return deletedCount == 1;
    }

    @Override
    public Habit get(int id, int userId) {
        Habit habit = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_HABIT_BY_ID_AND_USER_SQL);
            ps.setInt(1, id);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                habit = extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Exception occurs when retrieving all habits by user id:\n" + e.getMessage());
        }
        return habit;
    }

    @Override
    public List<Habit> getAll() {
        List<Habit> habits = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL_HABITS_SQL);
            while (resultSet.next()) {
                Habit habit = extractUserFromResultSet(resultSet);
                habits.add(habit);
            }
        } catch (SQLException e) {
            System.out.println("Exception occurs when retrieving all habits by user id:\n" + e.getMessage());
        }
        return habits;
    }

    @Override
    public List<Habit> getAllByUserId(Integer userId) {
        List<Habit> habits = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_ALL_HABITS_BY_USER_SQL);
            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Habit habit = extractUserFromResultSet(resultSet);
                habits.add(habit);
            }
        } catch (SQLException e) {
            System.out.println("Exception occurs when retrieving all habits by user id " + userId + ":\n" + e.getMessage());
        }
        return habits;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}