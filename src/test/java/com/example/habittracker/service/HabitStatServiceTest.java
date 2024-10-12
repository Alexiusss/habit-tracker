package com.example.habittracker.service;

import com.example.habittracker.repository.HabitStatRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HabitStatServiceTest {

    @Mock
    HabitStatRepository repository;

    @InjectMocks
    HabitStatService service;
}