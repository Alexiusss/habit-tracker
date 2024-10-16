package com.example.habittracker.service;

import com.example.habittracker.dto.HabitStatTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.HabitStat;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.HabitStatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Period;
import java.util.List;

import static com.example.habittracker.service.HabitStatTestData.FIRST_HABIT_STAT;
import static com.example.habittracker.service.HabitStatTestData.FIRST_HABIT_STAT_ID;
import static com.example.habittracker.util.HabitTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class HabitStatServiceTest {

    @Mock
    HabitRepository habitRepository;

    @Mock
    HabitStatRepository habitStatRepository;

    @InjectMocks
    HabitStatService service;

    @Test
    void markAsCompleted() {
        Mockito.when(habitStatRepository.save(any(HabitStat.class))).thenReturn(FIRST_HABIT_STAT);
        boolean isMarked = service.markAsCompleted(USER_ID, USER_ID);
        Assertions.assertThat(isMarked).isTrue();
    }

    @Test
    void deleteAllByUserIdAndHabitId() {
        Mockito.when(habitStatRepository.deleteAllByUserIdAndHabitId(USER_ID, FIRST_HABIT_STAT_ID)).thenReturn(true);
        service.deleteAllByUserIdAndHabitId(USER_ID, FIRST_HABIT_STAT_ID);

        Mockito.verify(habitStatRepository).deleteAllByUserIdAndHabitId(USER_ID, FIRST_HABIT_STAT_ID);
    }

    @Test
    void deleteNotFound() {
        Mockito.when(habitStatRepository.deleteAllByUserIdAndHabitId(USER_ID, NOT_FOUND_ID)).thenReturn(false);
        assertThrowsExactly(NotFoundException.class, () -> service.deleteAllByUserIdAndHabitId(USER_ID, NOT_FOUND_ID));
    }

    @Test
    void getCountOfSuccessfulStreaks() {
        Mockito.when(habitStatRepository.getAllByUserId(USER_ID)).thenReturn(List.of(FIRST_HABIT_STAT));
        Mockito.when(habitRepository.getAllByUserId(USER_ID)).thenReturn(List.of(FIRST_HABIT));

        int countOfSuccessfulStreaks = service.getCountOfSuccessfulStreaks(USER_ID);
        Assertions.assertThat(countOfSuccessfulStreaks).isEqualTo(1);
    }

    @Test
    void getPercentOfSuccessfulExecution() {
        Mockito.when(habitStatRepository.getAllByUserId(USER_ID)).thenReturn(List.of(FIRST_HABIT_STAT));
        Mockito.when(habitRepository.getAllByUserId(USER_ID)).thenReturn(List.of(FIRST_HABIT));
        int period = 7;

        List<HabitStatTo> percentOfSuccessfulExecution = service.getPercentOfSuccessfulExecution(USER_ID, Period.ofDays(period));
        Integer percentageOfCompletion = percentOfSuccessfulExecution.get(0).percentageOfCompletion();

        Assertions.assertThat(percentOfSuccessfulExecution.size()).isEqualTo(1);
        Assertions.assertThat(percentageOfCompletion).isEqualTo(100 / period);
    }
}