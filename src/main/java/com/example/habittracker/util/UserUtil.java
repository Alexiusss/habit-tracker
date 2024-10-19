package com.example.habittracker.util;

import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.model.Role;
import com.example.habittracker.model.User;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Utility class for mapping and handling user-related operations.
 * This class provides methods to convert between data transfer objects (DTOs) and the {@code User} entity,
 * as well as creating and updating {@code User} objects.
 *
 *  @author Alexey Boyarinov
 */
@UtilityClass
public class UserUtil {

    public static final String INSERT_USER_SQL = "INSERT INTO entity.users (version, name, email, password) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_USER_SQL = "UPDATE entity.users SET modified_at=?, version=?, name=?, email=?, password=?, is_active=? WHERE id=?";
    public static final String SELECT_ALL_USERS_SQL = "SELECT * FROM entity.users";
    public static final String SELECT_USER_BY_ID_SQL = "SELECT * FROM entity.users WHERE id=?";
    public static final String SELECT_USER_BY_EMAIL_SQL = "SELECT * FROM entity.users WHERE email=?";
    public static final String INSERT_USER_ROLES_SQL = "INSERT INTO entity.user_role (user_id, role) VALUES (?,?)";
    public static final String SELECT_USER_ROLES_BY_USER_ID = "SELECT * FROM entity.user_role WHERE user_id=?";

    /**
     * Converts a {@code User} entity to a {@code UserResponseTo} DTO.
     *
     * @param user the {@code User} entity to convert
     * @return the converted {@code UserResponseTo} DTO
     */
    public static UserResponseTo asTo(User user) {
        return new UserResponseTo(user.getId(), user.getName(), user.getEmail(), user.isActive());
    }

    /**
     * Creates a new {@code User} entity from a {@code UserRequestTo} DTO.
     *
     * @param userTo the {@code UserRequestTo} containing user details
     * @return the newly created {@code User} entity
     */
    public static User createNewFromTo(UserRequestTo userTo) {
        return User.builder()
                .createdAt(LocalDateTime.now())
                .modifiedAt(null)
                .version(0)
                .name(userTo.name())
                .email(userTo.email())
                .password(userTo.password())
                .isActive(true)
                .roles(Set.of(Role.USER))
                .build();
    }

    /**
     * Updates an existing {@code User} entity with data from a {@code UserRequestTo} DTO.
     *
     * @param user the existing {@code User} entity to update
     * @param userTo the {@code UserRequestTo} containing updated user details
     * @return the updated {@code User} entity
     */
    public static User updateFromTo(User user, UserRequestTo userTo) {
        user.setModifiedAt(LocalDateTime.now());
        user.setVersion(user.getVersion() + 1);
        user.setName(userTo.name());
        user.setEmail(userTo.email());
        user.setPassword(userTo.password());
        return user;
    }

    public static User extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("id"))
                .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                .version(resultSet.getInt("version"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .isActive(resultSet.getBoolean("is_active"))
                .build();
    }
}