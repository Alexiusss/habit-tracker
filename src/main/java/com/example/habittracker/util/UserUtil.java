package com.example.habittracker.util;

import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.model.Role;
import com.example.habittracker.model.User;
import lombok.experimental.UtilityClass;

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
}