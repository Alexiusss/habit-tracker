package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.service.impl.HabitServiceImpl;
import com.example.habittracker.util.SecurityUtil;

import java.time.Period;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

public class HabitEditorMenu implements Command {
    private final Integer habitId;
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final HabitServiceImpl habitService;
    private final CommandContext commandContext;

    public HabitEditorMenu(Integer habitId, CommandContext commandContext) {
        this.habitId = habitId;
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.invoker = commandContext.getInvoker();
        this.habitService = commandContext.getHabitService();
        this.commandContext = commandContext;
    }

    @Override
    public void execute() {
        printMenu();
        saveHabit();
    }

    private void printMenu() {
        System.out.print("""
                ==================================
                            Habit editor
                ==================================
                """);
        if (habitId != null) {
            HabitTo habit = habitService.get(habitId, securityUtil.getCurrentUserProfile().id());
            System.out.println("Name: " + habit.name());
            System.out.println("Frequency: " + (habit.frequency().getDays() == 1 ? "daily" : "weekly"));
            System.out.println("Status: " + (habit.isActive() ? "active" : "inactive"));
            System.out.println("==================================");
        }
    }

    private void saveHabit() {
        boolean isSaved = false;
        Integer userId = securityUtil.getCurrentUserProfile().id();

        while (!isSaved) {
            try {
                String name = userInputReader.getUserInput("name");
                int frequency = userInputReader.getUserInput("frequency (daily or weekly)").equals("daily") ? 1 : 7;
                if (habitId != null) {
                    boolean status = userInputReader.getUserInput("status (active or inactive)").equals("active");
                    habitService.update(new HabitTo(habitId, name, Period.ofDays(frequency), status, userId), userId);
                    isSaved = true;
                } else {
                    isSaved = habitService.create(new HabitTo(null, name, Period.ofDays(frequency), true, userId), userId) != null;
                }
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Habit saved\n");

        openMenu(invoker, new HabitMenu(commandContext));
    }
}
