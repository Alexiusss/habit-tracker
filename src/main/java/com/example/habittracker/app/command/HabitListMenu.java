package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.model.Habit;
import com.example.habittracker.service.HabitService;
import com.example.habittracker.service.HabitStatService;
import com.example.habittracker.util.SecurityUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

public class HabitListMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final HabitService habitService;
    private final HabitStatService habitStatService;
    private final CommandContext commandContext;

    public HabitListMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.invoker = commandContext.getInvoker();
        this.habitService = commandContext.getHabitService();
        this.habitStatService = commandContext.getHabitStatService();
        this.commandContext = commandContext;
    }

    @Override
    public void execute() {
        printMenu(Habit::isActive);
    }

    private void printMenu(Predicate<Habit> predicate) {
        System.out.print("""
                         Habits
                """);
        List<HabitTo> habits = habitService.getAllByUserId(securityUtil.getCurrentUserProfile().id(), predicate);
        printTable(habits);
        handleInput();
    }

    private void handleInput() {
        System.out.println("""
                1. Set filter
                2. Mark habit performed
                3. Return to main menu

                Enter your choice:\s""");

        boolean exit = false;
        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    setFilterAndPrint();
                    yield true;
                }
                case "2" -> {
                    markHabit();
                    yield true;
                }
                case "3" -> {
                    openMenu(invoker, new MainMenu(commandContext));
                    yield true;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    yield false;
                }
            };
        }
    }

    private void printTable(List<HabitTo> habits) {
        System.out.println("==================================================");
        if (habits.isEmpty()) {
            System.out.println("No habits have been added yet");
        } else
            System.out.printf("%5s %10s %12s %10s", "ID", "NAME", "FREQUENCY", "status");
        System.out.println();
        System.out.println("==================================================");


        for (HabitTo habit : habits) {
            System.out.format("%5s %12s %14s %10s", habit.id(), habit.name(), habit.frequency().getDays() == 1 ? " Daily" : "Weekly", habit.isActive() ? "Active" : "Inactive");
            System.out.println();
        }
        if (!habits.isEmpty()) {
            System.out.println("==================================================");
        }
    }

    private void setFilterAndPrint() {
        Predicate<Habit> predicate = null;

        while (predicate == null) {
            String paramType = userInputReader.getUserInput("type of parameter(date or status)");
            if (paramType.equals("date")) {
                String inputDate = userInputReader.getUserInput(" date in format like 2007-12-03");
                LocalDate date = LocalDate.parse(inputDate);
                predicate = habit -> habit.getCreatedAt().toLocalDate().equals(date);
            } else if (paramType.equals("status")) {
                String inputStatus = userInputReader.getUserInput(" status of habit (active or inactive)");
                if (inputStatus.equals("active")) {
                    predicate = Habit::isActive;
                } else if (inputStatus.equals("inactive")) {
                    predicate = habit -> !habit.isActive();
                }
            } else {
                System.out.println("Wrong input. Please try again.");
            }
        }

        printMenu(predicate);
    }

    private void markHabit() {
        boolean isMarked = false;

        while (!isMarked) {
            try {
                Integer habitId = Integer.parseInt(userInputReader.getUserInput("the id of habit to mark"));
                isMarked = habitStatService.markAsCompleted(securityUtil.getCurrentUserProfile().id(), habitId);
            } catch (IllegalArgumentException | NotFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again...");
            }
        }
        System.out.println("Habit successfully marked");
        handleInput();
    }
}