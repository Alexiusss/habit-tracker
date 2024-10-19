package com.example.habittracker.repository.jdbc;

import com.example.habittracker.exception.DuplicateEmailException;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static com.example.habittracker.util.UserTestData.*;

@DisplayName("JDBC user repository test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcUserRepositoryTest extends AbstractJdbcRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JdbcUserRepository(dataSource);
    }

    @Test
    @DisplayName("Create a new user, expected success")
    void create() throws SQLException {
        User newUser = NEW_USER;
        User saved = userRepository.save(newUser);
        newUser.setId(saved.getId());

        Assertions.assertThat(saved)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(newUser);
    }

    @Test
    @DisplayName("Create a new user with an empty password, expected fail")
    void createInvalid() {
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> userRepository.save(NEW_INVALID_USER))
                .withMessageContaining("ERROR: null value in column \"password\" violates not-null constraint");
    }

    @Test
    @DisplayName("Create a new user with an already existing email, expected fail")
    void createWithDuplicateEmail() {
        Assertions.assertThatExceptionOfType(DuplicateEmailException.class)
                .isThrownBy(() -> userRepository.save(NEW_USER_WITH_DUPLICATE_EMAIL))
                .withMessageContaining("User with email" + ADMIN_EMAIL + " already exists");
    }

    @Test
    @Order(2)
    @DisplayName("Update an existing user, expected success")
    void update() throws SQLException {
        Assertions.assertThat(userRepository.save(UPDATED_USER)).isNull();

        User userFromDB = userRepository.get(USER.getId());
        Assertions.assertThat(userFromDB)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt", "roles")
                .isEqualTo(UPDATED_USER);
    }

    @Test
    @DisplayName("Delete user by ID, expected success")
    void delete() throws SQLException {
        boolean isDeleted = userRepository.delete(USER.getId());

        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    @DisplayName("Delete user by ID, expected fail")
    void deleteNotFound() throws SQLException {
        boolean isDeleted = userRepository.delete(NOT_FOUND_ID);

        Assertions.assertThat(isDeleted).isFalse();
    }

    @Test
    @DisplayName("Get user by ID, expected success")
    void get() {
        User user = userRepository.get(ADMIN_ID);

        Assertions.assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(ADMIN);
    }

    @Test
    @DisplayName("Get user by ID, expected fail")
    void getNotFound() {
        User user = userRepository.get(NOT_FOUND_ID);

        Assertions.assertThat(user).isNull();
    }

    @Test
    @DisplayName("Get user by email, expected success")
    void getByEmail() {
        User user = userRepository.getByEmail(ADMIN_EMAIL);

        Assertions.assertThat(user)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(ADMIN);
    }

    @Test
    @DisplayName("Get user by email, expected fail")
    void getNotFoundByEmail() {
        User user = userRepository.getByEmail("some_mail@ya.ru");

        Assertions.assertThat(user).isEqualTo(null);
    }

    @Test
    @Order(1)
    @DisplayName("Get all users, expected success")
    void getAll() {
        List<User> users = userRepository.getAll();

        Assertions.assertThat(users)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt", "roles")
                .isEqualTo(List.of(ADMIN, USER));
    }
}