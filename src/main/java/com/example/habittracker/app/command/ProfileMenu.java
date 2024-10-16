package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.util.SecurityUtil;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

public class ProfileMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final CommandContext commandContext;

    public ProfileMenu(CommandContext commandContext) {
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
        System.out.println("""
                ==============================
                          Profile
                ==============================
                1. Profile editor
                2. Delete account
                3. Return to main menu

                Enter your choice:\s""");
    }

    private void handleInput() {
        boolean exit = false;

        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    openMenu(invoker, new ProfileEditorMenu(commandContext));
                    yield true;
                }
                case "2" -> {
                    deleteAccount();
                    openMenu(invoker, new StartMenu(commandContext));
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

    public void deleteAccount() {
        securityUtil.deleteAccount();
        System.out.println("Account successfully deleted");
    }
}
