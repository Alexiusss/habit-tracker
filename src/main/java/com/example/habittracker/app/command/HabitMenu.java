package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.service.HabitService;
import com.example.habittracker.util.SecurityUtil;

import java.util.List;

public class HabitMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final HabitService habitService;
    private final CommandContext commandContext;

    public HabitMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.invoker = commandContext.getInvoker();
        this.habitService = commandContext.getHabitService();
        this.commandContext = commandContext;
    }

    @Override
    public void execute() {
        printMenu();

        boolean exit = false;

        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    invoker.setCommand(new HabitEditor(null, commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                case "2" -> {
                    int id = getHabitIdFromInput("editing");
                    invoker.setCommand(new HabitEditor(id, commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                case "3" -> {
                    deleteHabit();
                    invoker.setCommand(new HabitMenu(commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                //case "4" -> true;
                case "5" -> {
                    invoker.setCommand(new StatisticsAndAnalyticsMenu(commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                case "6" -> {
                    invoker.setCommand(new MainMenu(commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    yield false;
                }
            };
        }
    }

    private void printMenu() {
        List<HabitTo> habits = habitService.getAllByUserId(securityUtil.getCurrentUserProfile().id());
        System.out.print("""
                            Habits
                """);
        printTable(habits);
        System.out.println("""
                1. Create
                2. Edit
                3. Delete
                4. Get filtered list
                5. Statistics and analytics
                6. Return to main menu

                Enter your choice:\s""");
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

    private int getHabitIdFromInput(String message) {
        HabitTo habit = null;
        while (habit == null) {
            try {
                String id = userInputReader.getUserInput("id of habit for " + message);
                int habitId = Integer.parseInt(id);
                habit = habitService.get(habitId, securityUtil.getCurrentUserProfile().id());
            } catch (NullPointerException | NumberFormatException | NotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        return habit.id();
    }

    private void deleteHabit() {
        int id = getHabitIdFromInput("deleting");
        habitService.delete(id, securityUtil.getCurrentUserProfile().id());
        System.out.println("Habit deleted");
    }
}

