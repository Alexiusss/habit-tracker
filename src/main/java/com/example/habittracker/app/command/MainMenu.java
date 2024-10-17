package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.util.SecurityUtil;

import static com.example.habittracker.util.ConsoleUtil.openMenu;
import static com.example.habittracker.util.print.MenuPrinterUtil.printMainMenu;

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
        printMainMenu(securityUtil.isAdmin());
        handleInput();
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
