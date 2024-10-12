package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.util.SecurityUtil;

public class MainMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final CommandContext commandContext;

    public MainMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.invoker = commandContext.getInvoker();
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
                    invoker.setCommand(new HabitMenu(commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                case "2" -> {
                    invoker.setCommand(new ProfileMenu(commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                case "3" -> {
                    invoker.setCommand(new AdminMenu(commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                case "exit", "q" -> true;
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    yield false;
                }
            };
        }
    }

    private void printMenu() {
        System.out.print("""
                ==========================
                        Main menu
                ==========================
                1. Habits
                2. Profile
                """);
        if (securityUtil.isAdmin()) {
            System.out.println("""
                3. Admin panel
                """);
        }
        System.out.println("""                
                Type exit or q to quit
                                 
                Enter your choice:\s
                """);

    }
}
