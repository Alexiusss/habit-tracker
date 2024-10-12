package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.repository.inmemory.HabitRepositoryInMemory;
import com.example.habittracker.repository.inmemory.HabitStatRepositoryInMemory;
import com.example.habittracker.repository.inmemory.UserRepositoryInMemory;
import com.example.habittracker.service.HabitService;
import com.example.habittracker.service.HabitStatService;
import com.example.habittracker.service.IUserService;
import com.example.habittracker.service.UserService;
import com.example.habittracker.util.SecurityUtil;
import lombok.Getter;

@Getter
public class CommandContext {
    private final Invoker invoker;
    private final UserInputReader userInputReader;
    private final UserRepository userRepository;
    private final IUserService userService;
    private final SecurityUtil securityUtil;
    private final HabitRepository habitRepository;
    private final HabitService habitService;
    private final HabitStatService habitStatService;

    public CommandContext() {
        invoker = new Invoker();
        userInputReader = new UserInputReader();
        userRepository = new UserRepositoryInMemory();
        userService = new UserService(userRepository);
        securityUtil = new SecurityUtil(userRepository, userService);
        habitRepository = new HabitRepositoryInMemory();
        habitService = new HabitService(habitRepository);
        habitStatService = new HabitStatService(habitRepository, new HabitStatRepositoryInMemory());
    }
}
