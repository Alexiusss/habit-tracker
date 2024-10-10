package com.example.habittracker.service;

import com.example.habittracker.dto.in.UserRequestTo;
import com.example.habittracker.dto.out.UserResponseTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.util.UserTestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void get() {
        Mockito.when(userRepository.get(UserTestData.ADMIN_ID)).thenReturn(UserTestData.ADMIN);
        UserResponseTo user = userService.get(UserTestData.ADMIN_ID);

        Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(UserTestData.ADMIN_FROM_DB);
    }

    @Test
    void getNotFound() {
        Mockito.when(userRepository.get(UserTestData.NOT_FOUND_ID)).thenThrow(new NotFoundException("Not found entity with " + UserTestData.NOT_FOUND_ID));
        assertThrowsExactly(NotFoundException.class, () -> userService.get(UserTestData.NOT_FOUND_ID));
    }

    @Test
    void getByEmail() {
        Mockito.when(userRepository.getByEmail(UserTestData.ADMIN_EMAIL)).thenReturn(UserTestData.ADMIN);
        UserResponseTo user = userService.getByEmail(UserTestData.ADMIN_EMAIL);

        Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(UserTestData.ADMIN_FROM_DB);
    }

    @Test
    void getAll() {
        Mockito.when(userRepository.getAll()).thenReturn(List.of(UserTestData.ADMIN));
        List<UserResponseTo> users = userService.getAll();

        Assertions.assertThat(users)
                .usingRecursiveComparison()
                .isEqualTo(List.of(UserTestData.ADMIN_FROM_DB));
    }

    @Test
    void create() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(UserTestData.ADMIN);
        UserResponseTo savedUser = userService.create(UserTestData.NEW_USER);

        Assertions.assertThat(savedUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(UserTestData.ADMIN_FROM_DB);
    }

    @Test
    void createInvalid() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(UserTestData.ADMIN);
        UserResponseTo savedUser = userService.create(UserTestData.NEW_USER);

        Assertions.assertThat(savedUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(UserTestData.ADMIN_FROM_DB);
    }

    @Test
    void update() {
        Mockito.when(userRepository.get(UserTestData.USER.getId())).thenReturn(UserTestData.USER);
        User updatedUser = UserTestData.USER;
        updatedUser.setName("Updated name");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        userService.update(UserTestData.UPDATED_USER);

        Mockito.verify(userRepository).save(updatedUser);
        Mockito.verify(userRepository).save(ArgumentMatchers.argThat(argument -> argument.getName().equals("Updated name")));
    }


    @Test
    void delete() {
        Mockito.when(userRepository.delete(UserTestData.ADMIN_ID)).thenReturn(true);
        userService.delete(UserTestData.ADMIN_ID);

        Mockito.verify(userRepository).delete(UserTestData.ADMIN_ID);
    }

    @Test
    void deleteNotFound() {
        Mockito.when(userRepository.delete(UserTestData.NOT_FOUND_ID)).thenReturn(false);
        assertThrowsExactly(NotFoundException.class, () -> userService.delete(UserTestData.NOT_FOUND_ID));
    }
}