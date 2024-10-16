package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.service.HabitService;
import com.example.habittracker.util.SecurityUtil;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

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
        handleInput();
    }

    private void handleInput() {
        boolean exit = false;

        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    openMenu(invoker, new HabitEditorMenu(null, commandContext));
                    yield true;
                }
                case "2" -> {
                    int id = getHabitIdFromInput("editing");
                    openMenu(invoker, new HabitEditorMenu(id, commandContext));
                    yield true;
                }
                case "3" -> {
                    deleteHabit();
                    openMenu(invoker, this);
                    yield true;
                }
                case "4" -> {
                    openMenu(invoker, new HabitListMenu(commandContext));
                    yield true;
                }
                case "5" -> {
                    openMenu(invoker, new StatisticsAndAnalyticsMenu(commandContext));
                    yield true;
                }
                case "6" -> {
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

    private void printMenu() {
        System.out.print("""
                        Habits menu
                """);
        System.out.println("""
                1. Create
                2. Edit
                3. Delete
                4. Get filtered list
                5. Statistics and analytics
                6. Return to main menu

                Enter your choice:\s""");
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

