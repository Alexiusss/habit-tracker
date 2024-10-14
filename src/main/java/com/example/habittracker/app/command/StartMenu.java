package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import lombok.AllArgsConstructor;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

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
                    openMenu(invoker, new LoginMenu(commandContext));
                    yield true;
                }
                case "2" -> {
                    openMenu(invoker, new RegisterMenu(commandContext));
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
