package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.repository.HabitRepository;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.repository.inmemory.HabitRepositoryInMemory;
import com.example.habittracker.repository.inmemory.HabitStatRepositoryInMemory;
import com.example.habittracker.repository.inmemory.UserRepositoryInMemory;
import com.example.habittracker.repository.jdbc.JdbcHabitRepository;
import com.example.habittracker.repository.jdbc.JdbcHabitStatRepository;
import com.example.habittracker.repository.jdbc.JdbcUserRepository;
import com.example.habittracker.service.impl.HabitServiceImpl;
import com.example.habittracker.service.impl.HabitStatServiceImpl;
import com.example.habittracker.service.UserService;
import com.example.habittracker.service.impl.UserServiceImpl;
import com.example.habittracker.util.SecurityUtil;
import lombok.Getter;

import javax.sql.DataSource;

@Getter
public class CommandContext {
    private final Invoker invoker;
    private final UserInputReader userInputReader;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SecurityUtil securityUtil;
    private final HabitRepository habitRepository;
    private final HabitServiceImpl habitService;
    private final HabitStatServiceImpl habitStatService;

    public CommandContext() {
        invoker = new Invoker();
        userInputReader = new UserInputReader();
        userRepository = new UserRepositoryInMemory();
        userService = new UserServiceImpl(userRepository);
        securityUtil = new SecurityUtil(userRepository, userService);
        habitRepository = new HabitRepositoryInMemory();
        habitService = new HabitServiceImpl(habitRepository);
        habitStatService = new HabitStatServiceImpl(habitRepository, new HabitStatRepositoryInMemory());
    }

    public CommandContext(DataSource dataSource) {
        invoker = new Invoker();
        userInputReader = new UserInputReader();
        userRepository = new JdbcUserRepository(dataSource);
        userService = new UserServiceImpl(userRepository);
        securityUtil = new SecurityUtil(userRepository, userService);
        habitRepository = new JdbcHabitRepository(dataSource);
        habitService = new HabitServiceImpl(habitRepository);
        habitStatService = new HabitStatServiceImpl(habitRepository, new JdbcHabitStatRepository(dataSource));
    }
}
