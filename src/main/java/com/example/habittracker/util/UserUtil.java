package com.example.habittracker.util;

import com.example.habittracker.dto.in.UserRequestTo;
import com.example.habittracker.dto.out.UserResponseTo;
import com.example.habittracker.model.Role;
import com.example.habittracker.model.User;

import java.time.LocalDateTime;
import java.util.Set;

public class UserUtil {

    public static UserResponseTo asTo(User user) {
        return new UserResponseTo(user.getId(), user.getName(), user.getEmail());
    }

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

    public static User updateFromTo(User user, UserRequestTo userTo) {
        user.setModifiedAt(LocalDateTime.now());
        user.setVersion(user.getVersion() + 1);
        user.setName(userTo.name());
        user.setEmail(userTo.email());
        user.setPassword(userTo.password());
        return user;
    }
}