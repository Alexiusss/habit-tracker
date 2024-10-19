package com.example.habittracker.util;

import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.model.Role;
import com.example.habittracker.model.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class UserTestData {
    public static final Integer ADMIN_ID = 1;
    public static final String ADMIN_NAME = "Admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String ADMIN_PASSWORD = "admin777";
    public static final Integer NOT_FOUND_ID = 1000;

    public static final User ADMIN = User.builder()
            .id(ADMIN_ID)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .name(ADMIN_NAME)
            .email(ADMIN_EMAIL)
            .password(ADMIN_PASSWORD)
            .isActive(true)
            .roles(Set.of(Role.ADMIN, Role.USER))
            .build();

    public static final User USER = User.builder()
            .id(2)
            .createdAt(LocalDateTime.now())
            .modifiedAt(null)
            .version(0)
            .name("User")
            .email("user@gmail.com")
            .password("user555")
            .isActive(true)
            .roles(Set.of(Role.USER))
            .build();

    public static final UserResponseTo ADMIN_FROM_DB = new UserResponseTo(ADMIN_ID, ADMIN_NAME, ADMIN_EMAIL, true);

    public static final UserRequestTo NEW_USER = new UserRequestTo(null, ADMIN_NAME, ADMIN_EMAIL, ADMIN_PASSWORD);
    public static final UserRequestTo UPDATED_USER = new UserRequestTo(USER.getId(), "Updated name", USER.getEmail(), USER.getPassword());
}