package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.util.SecurityUtil;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

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
        handleInput();
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

    private void handleInput() {
        boolean exit = false;

        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    openMenu(invoker, new HabitMenu(commandContext));
                    yield true;
                }
                case "2" -> {
                    openMenu(invoker, new ProfileMenu(commandContext));
                    yield true;
                }
                case "3" -> {
                    if (securityUtil.isAdmin()) {
                        openMenu(invoker, new AdminMenu(commandContext));
                        yield true;
                    } else {
                        yield false;
                    }
                }
                case "exit", "q" -> true;
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    yield false;
                }
            };
        }
    }
}
