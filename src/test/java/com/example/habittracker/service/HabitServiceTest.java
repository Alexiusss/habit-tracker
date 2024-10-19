package com.example.habittracker.service;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.service.impl.HabitServiceImpl;
import com.example.habittracker.util.HabitTestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@DisplayName("Habit service test")
@ExtendWith(MockitoExtension.class)
public class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitServiceImpl habitService;

    @Test
    @DisplayName("Get habit by ID, expected success")
    void get() {
        Mockito.when(habitRepository.get(HabitTestData.FIRST_HABIT_ID, HabitTestData.USER_ID)).thenReturn(HabitTestData.FIRST_HABIT);
        HabitTo habit = habitService.get(HabitTestData.FIRST_HABIT_ID, HabitTestData.USER_ID);

        Assertions.assertThat(habit)
                .usingRecursiveComparison()
                .isEqualTo(HabitTestData.FIRST_HABIT_FROM_DB);
    }

    @Test
    @DisplayName("Get habit by ID, throws NotFoundException")
    void getNotFound() {
        Mockito.when(habitRepository.get(HabitTestData.NOT_FOUND_ID, HabitTestData.USER_ID)).thenThrow(new NotFoundException("Not found entity with " + HabitTestData.NOT_FOUND_ID));
        assertThrowsExactly(NotFoundException.class, () -> habitService.get(HabitTestData.NOT_FOUND_ID, HabitTestData.USER_ID));
    }

    @Test
    @DisplayName("Get all habits, expected success")
    void getAll() {
        Mockito.when(habitRepository.getAll()).thenReturn(List.of(HabitTestData.FIRST_HABIT, HabitTestData.SECOND_HABIT));
        List<HabitTo> habits = habitService.getAll();

        Assertions.assertThat(habits)
                .usingRecursiveComparison()
                .isEqualTo(List.of(HabitTestData.FIRST_HABIT_FROM_DB, HabitTestData.SECOND_HABIT));
    }

    @Test
    @DisplayName("Create a new habit, expected success")
    void create() {
        Mockito.when(habitRepository.save(any(Habit.class), eq(HabitTestData.USER_ID))).thenReturn(HabitTestData.FIRST_HABIT);
        HabitTo savedHabit = habitService.create(HabitTestData.NEW_HABIT_TO, HabitTestData.USER_ID);

        Assertions.assertThat(savedHabit)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(HabitTestData.FIRST_HABIT_FROM_DB);
    }

    @Test
    @DisplayName("Update an existing habit, expected success")
    void update() {
        Mockito.when(habitRepository.get(eq(HabitTestData.SECOND_HABIT.getId()),eq(HabitTestData.USER_ID))).thenReturn(HabitTestData.SECOND_HABIT);
        Habit updatedHabit = HabitTestData.SECOND_HABIT;
        updatedHabit.setName("Updated name");
        Mockito.when(habitRepository.save(any(Habit.class), eq(HabitTestData.USER_ID))).thenReturn(updatedHabit);

        habitService.update(HabitTestData.UPDATED_HABIT, HabitTestData.USER_ID);

        Mockito.verify(habitRepository).save(updatedHabit, HabitTestData.USER_ID);
    }


    @Test
    @DisplayName("Delete habit by its ID and user ID, expected success")
    void delete() {
        Mockito.when(habitRepository.delete(HabitTestData.FIRST_HABIT_ID, HabitTestData.USER_ID)).thenReturn(true);
        habitService.delete(HabitTestData.FIRST_HABIT_ID, HabitTestData.USER_ID);

        Mockito.verify(habitRepository).delete(HabitTestData.FIRST_HABIT_ID, HabitTestData.USER_ID);
    }

    @Test
    @DisplayName("Delete user by its ID and user ID, throws NotFoundException")
    void deleteNotFound() {
        Mockito.when(habitRepository.delete(HabitTestData.NOT_FOUND_ID, HabitTestData.USER_ID)).thenReturn(false);
        assertThrowsExactly(NotFoundException.class, () -> habitService.delete(HabitTestData.NOT_FOUND_ID, HabitTestData.USER_ID));
    }

}
