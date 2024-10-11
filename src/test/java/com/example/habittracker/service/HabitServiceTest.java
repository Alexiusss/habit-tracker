package com.example.habittracker.service;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.util.HabitTestData;
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
public class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitService habitService;

    @Test
    void get() {
        Mockito.when(habitRepository.get(HabitTestData.FIRST_HABIT_ID)).thenReturn(HabitTestData.FIRST_HABIT);
        HabitTo habit = habitService.get(HabitTestData.FIRST_HABIT_ID);

        Assertions.assertThat(habit)
                .usingRecursiveComparison()
                .isEqualTo(HabitTestData.FIRST_HABIT_FROM_DB);
    }

    @Test
    void getNotFound() {
        Mockito.when(habitRepository.get(HabitTestData.NOT_FOUND_ID)).thenThrow(new NotFoundException("Not found entity with " + HabitTestData.NOT_FOUND_ID));
        assertThrowsExactly(NotFoundException.class, () -> habitService.get(HabitTestData.NOT_FOUND_ID));
    }

    @Test
    void getAll() {
        Mockito.when(habitRepository.getAll()).thenReturn(List.of(HabitTestData.FIRST_HABIT, HabitTestData.SECOND_HABIT));
        List<HabitTo> habits = habitService.getAll();

        Assertions.assertThat(habits)
                .usingRecursiveComparison()
                .isEqualTo(List.of(HabitTestData.FIRST_HABIT_FROM_DB, HabitTestData.SECOND_HABIT));
    }

    @Test
    void create() {
        Mockito.when(habitRepository.save(any(Habit.class))).thenReturn(HabitTestData.FIRST_HABIT);
        HabitTo savedHabit = habitService.create(HabitTestData.NEW_HABIT);

        Assertions.assertThat(savedHabit)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(HabitTestData.FIRST_HABIT_FROM_DB);
    }

    @Test
    void update() {
        Mockito.when(habitRepository.get(HabitTestData.SECOND_HABIT.getId())).thenReturn(HabitTestData.SECOND_HABIT);
        Habit updatedHabit = HabitTestData.SECOND_HABIT;
        updatedHabit.setName("Updated name");
        Mockito.when(habitRepository.save(any(Habit.class))).thenReturn(updatedHabit);

        habitService.update(HabitTestData.UPDATED_HABIT);

        Mockito.verify(habitRepository).save(updatedHabit);
    }


    @Test
    void delete() {
        Mockito.when(habitRepository.delete(HabitTestData.FIRST_HABIT_ID)).thenReturn(true);
        habitService.delete(HabitTestData.FIRST_HABIT_ID);

        Mockito.verify(habitRepository).delete(HabitTestData.FIRST_HABIT_ID);
    }

    @Test
    void deleteNotFound() {
        Mockito.when(habitRepository.delete(HabitTestData.NOT_FOUND_ID)).thenReturn(false);
        assertThrowsExactly(NotFoundException.class, () -> habitService.delete(HabitTestData.NOT_FOUND_ID));
    }

}
