package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.util.SecurityUtil;

public class AdminMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final CommandContext commandContext;

    public AdminMenu(CommandContext commandContext) {
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
        System.out.println("""
                ==============================
                        Admin menu
                 ==============================
                1. Return to main menu
                
                Type exit or q to quit

                Enter your choice:\s""");
    }
}
