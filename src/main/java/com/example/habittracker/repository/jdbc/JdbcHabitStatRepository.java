package com.example.habittracker.repository.jdbc;

import com.example.habittracker.model.HabitStat;
import com.example.habittracker.repository.HabitStatRepository;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.habittracker.util.HabitStatUtil.*;

@AllArgsConstructor
public class JdbcHabitStatRepository implements HabitStatRepository {

    DataSource dataSource;

    @Override
    public HabitStat save(HabitStat habitStat) {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(INSERT_HABIT_STAT_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, 0);
            ps.setInt(2, habitStat.getUserId());
            ps.setInt(3, habitStat.getHabitId());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt("id");
                habitStat.setId(id);
            }
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Exception occurs while saving habit " + habitStat + ":\n" + e.getMessage());
        }
        return habitStat;
    }

    @Override
    public boolean deleteAllByUserIdAndHabitId(Integer userId, Integer habitId) {
        Connection connection = getConnection();
        int deletedCount = 0;
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("DELETE FROM entity.habit_stat WHERE user_id=? AND habit_id=?");
            ps.setInt(1, userId);
            ps.setInt(2, habitId);
            deletedCount = ps.executeUpdate();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Exception occurs when deleting all habit stats by user id " + userId + ":\n" + e.getMessage());
        }
        return deletedCount != 0;
    }

    @Override
    public List<HabitStat> getAllByUserId(Integer userId) {
        List<HabitStat> habitStatList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(GET_ALL_STATS_BY_USER_ID_SQL);
            ps.setInt(1, userId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                habitStatList.add(extractHabitStatFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Exception occurs when retrieving all habit stats by user id " + userId + ":\n" + e.getMessage());
        }
        return habitStatList;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}