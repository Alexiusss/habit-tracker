package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.service.impl.HabitStatServiceImpl;
import com.example.habittracker.util.SecurityUtil;

import java.time.Period;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

public class StatisticsAndAnalyticsMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final HabitStatServiceImpl habitStatService;
    private final CommandContext commandContext;

    public StatisticsAndAnalyticsMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.invoker = commandContext.getInvoker();
        this.habitStatService = commandContext.getHabitStatService();
        this.commandContext = commandContext;
    }

    @Override
    public void execute() {
        printMenu();
        handleInput();
    }

    void printMenu() {
        System.out.print("""
                 ==============================
                    Statistics and analytics
                 ==============================
                 1. Get count of current series of execution(streak)
                 2. Get success statistics for period
                 3. Back to main menu
                 
                Enter your choice:
                \s""");
    }

    void handleInput() {
        boolean exit = false;

        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    printHabitStreak();
                    openMenu(invoker, this);
                    yield true;
                }
                case "2" -> {
                    printHabitPercentageStatistics();
                    openMenu(invoker, this);
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

    private void printHabitStreak() {
        int countOfSuccessfulStreaks = habitStatService.getCountOfSuccessfulStreaks(getCurrentUserId());
        System.out.println("Current count of successful streak: " + countOfSuccessfulStreaks);
        System.out.println("""
                \s""");
    }

    private void printHabitPercentageStatistics() {
        Period period = null;
        while (period == null) {
            try {
                String userInput = userInputReader.getUserInput(" period of required statistics(1, 7 or 30 days)");
                period = Period.ofDays(Integer.parseInt(userInput));
                habitStatService.getPercentOfSuccessfulExecution(getCurrentUserId(), period)
                        .forEach(hs -> System.out.println(hs.name() + " - " + hs.percentageOfCompletion() + "%"));
            } catch (IllegalArgumentException | NotFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again...");
            }
            System.out.println("""
                    \s""");
        }
    }

    private int getCurrentUserId() {
        return securityUtil.getCurrentUserProfile().id();
    }
}
