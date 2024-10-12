package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StartMenu implements Command {
    private final UserInputReader userInputReader;
    private final Invoker invoker;

    CommandContext commandContext;

    public StartMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
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
                    invoker.setCommand(new LoginMenu(commandContext));
                    invoker.executeCommand();
                    yield true;
                }
                case "2" -> {
                    invoker.setCommand(new RegisterMenu(commandContext));
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
        System.out.println("""
                ==============================
                      Habit tracker app
                ==============================
                1. Sign in
                2. Register

                Type exit or q to quit

                Enter your choice:\s""");
    }
}
