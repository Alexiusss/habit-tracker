package com.example.habittracker.repository.jdbc;

import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static com.example.habittracker.util.HabitTestData.*;
import static com.example.habittracker.util.UserTestData.ADMIN_ID;

@DisplayName("JDBC habit repository test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcHabitRepositoryTest extends AbstractJdbcRepositoryTest{

    HabitRepository repository;

    @BeforeEach
    void setUp() {
        repository = new JdbcHabitRepository(dataSource);
    }
    @Test
    @DisplayName("Create a new habit, expected success")
    void create() {
        Habit saved = repository.save(NEW_HABIT, ADMIN_ID);
        NEW_HABIT.setId(saved.getId());

        Assertions.assertThat(saved)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(NEW_HABIT);
    }

    @Test
    @DisplayName("Update an existing habit, expected success")
    void update() throws SQLException {
        Assertions.assertThat(repository.save(UPDATED_HABIT_1, USER_ID)).isNull();

        Habit habitFromDB = repository.get(FIRST_HABIT_ID, USER_ID);
        Assertions.assertThat(habitFromDB)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(UPDATED_HABIT_1);
    }

    @Test
    @DisplayName("Delete a habit by user ID, expected success")
    void delete() {
        boolean isDeleted = repository.delete(SECOND_HABIT_ID, USER_ID);

        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    @Order(3)
    @DisplayName("Get a habits by user ID, expected success")
    void get() {
        Habit habit = repository.get(FIRST_HABIT_ID, USER_ID);

        Assertions.assertThat(habit)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(FIRST_HABIT);
    }

    @Test
    @Order(1)
    @DisplayName("Get all habits, expected success")
    void getAll() {
        List<Habit> habits = repository.getAll();

        Assertions.assertThat(habits)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(List.of(FIRST_HABIT, SECOND_HABIT));
    }

    @Test
    @Order(2)
    @DisplayName("Get all habits by user ID, expected success")
    void getAllByUserId() {
        List<Habit> habits = repository.getAllByUserId(USER_ID);

        Assertions.assertThat(habits)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(List.of(FIRST_HABIT, SECOND_HABIT));
    }
}