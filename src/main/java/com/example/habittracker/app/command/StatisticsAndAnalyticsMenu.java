package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.service.HabitService;
import com.example.habittracker.util.SecurityUtil;

public class StatisticsAndAnalyticsMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final HabitService habitService;
    private final CommandContext commandContext;

    public StatisticsAndAnalyticsMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.invoker = commandContext.getInvoker();
        this.habitService = commandContext.getHabitService();
        this.commandContext = commandContext;
    }

    @Override
    public void execute() {
        printMenu();

    }

    void printMenu() {
        System.out.print("""
                 ==============================
                    Statistics and analytics
                 ==============================
                """);
    }
}
