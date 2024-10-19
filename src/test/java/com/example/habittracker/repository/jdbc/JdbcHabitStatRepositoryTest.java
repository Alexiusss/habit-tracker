package com.example.habittracker.repository.jdbc;

import com.example.habittracker.model.HabitStat;
import com.example.habittracker.repository.HabitStatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.example.habittracker.util.HabitStatTestData.*;
import static com.example.habittracker.util.HabitTestData.FIRST_HABIT_ID;
import static com.example.habittracker.util.UserTestData.USER;

@DisplayName("JDBC habit stat repository test")
class JdbcHabitStatRepositoryTest extends AbstractJdbcRepositoryTest {

    HabitStatRepository repository;

    @BeforeEach
    void setUp() {
        repository = new JdbcHabitStatRepository(dataSource);
    }

    @Test
    @DisplayName("Mark habit as completed for today, expected success")
    void save() {
        HabitStat saved = repository.save(NEW_HABIT_STAT);
        NEW_HABIT_STAT.setId(saved.getId());
        Assertions.assertThat(saved)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(NEW_HABIT_STAT);
    }

    @Test
    @DisplayName("Delete all by user ID and habit ID, expected success")
    void deleteAllByUserIdAndHabitId() {
        boolean isDeleted = repository.deleteAllByUserIdAndHabitId(USER.getId(), FIRST_HABIT_ID);
        Assertions.assertThat(isDeleted).isTrue();
    }

    @Test
    @DisplayName("Get all habit stats by user ID, expected success")
    void getAllByUserId() {
        List<HabitStat> habitStats = repository.getAllByUserId(USER.getId());

        Assertions.assertThat(habitStats)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "modifiedAt")
                .isEqualTo(List.of(FIRST_HABIT_STAT, SECOND_HABIT_STAT));
    }
}