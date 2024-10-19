package com.example.habittracker.repository.jdbc;

import com.example.habittracker.exception.DuplicateEmailException;
import com.example.habittracker.model.Role;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.habittracker.util.UserUtil.*;

@AllArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private final DataSource dataSource;

    @Override
    public User save(User user) throws SQLException {
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            if (user.isNew()) {
                PreparedStatement ps = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, 0);
                ps.setString(2, user.getName());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPassword());
                ps.executeUpdate();
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt("id");
                    user.setId(id);
                    saveRoles(connection, id, user.getRoles());
                }
                connection.commit();
                return user;
            } else {
                PreparedStatement ps = connection.prepareStatement(UPDATE_USER_SQL);
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(2, user.getVersion());
                ps.setString(3, user.getName());
                ps.setString(4, user.getEmail());
                ps.setString(5, user.getPassword());
                ps.setBoolean(6, user.isActive());
                ps.setInt(7, user.getId());
                ps.executeUpdate();
                connection.commit();
                return null;
            }
        } catch (SQLException e) {
            connection.rollback();
            if (e.getMessage().contains("users_unique_email_idx")) {
                throw new DuplicateEmailException("User with email" + user.getEmail() + " already exists");
            }
            throw new RuntimeException("Exception occurred when saving to the JDBC User Repository: \n" + e.getMessage());
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        int deletedCount;
        Connection connection = getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("DELETE FROM entity.users WHERE id=?");
            ps.setInt(1, id);
            deletedCount = ps.executeUpdate();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException("Exception while deleting user with id: \n" + e.getMessage());
        } finally {
            connection.close();
        }
        return deletedCount == 1;
    }

    @Override
    public User get(int id) {
        User user = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_ID_SQL);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user = extractUserFromResultSet(resultSet);
                user.setRoles(getRoles(connection, id));
            }
        } catch (SQLException e) {
            System.out.println("Exception in getById(): \n" + e.getMessage());
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_EMAIL_SQL);
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                user = extractUserFromResultSet(resultSet);
                user.setRoles(getRoles(connection, user.getId()));
            }
        } catch (SQLException e) {
            System.out.println("Exception in getByEmail(): \n" + e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_SQL);
            while (resultSet.next()) {
                users.add(extractUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Exception in getAll(): \n" + e.getMessage());
        }
        return users;
    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveRoles(Connection connection, int userId, Set<Role> roles) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement(INSERT_USER_ROLES_SQL);
            for (Role role : roles) {
                ps.setInt(1, userId);
                ps.setString(2, role.toString());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Set<Role> getRoles(Connection connection, int id) throws SQLException {
        Set<Role> roles = new HashSet<>();
        try {
            PreparedStatement ps = connection.prepareStatement(SELECT_USER_ROLES_BY_USER_ID);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roles.add(Role.valueOf(rs.getString("role")));
            }
        } catch (SQLException e) {
            connection.rollback();
        }
        return roles;
    }
}
