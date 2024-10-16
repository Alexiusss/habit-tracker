package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.service.HabitService;
import com.example.habittracker.service.IUserService;
import com.example.habittracker.util.PrintUtil;
import com.example.habittracker.util.SecurityUtil;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

public class AdminMenu implements Command {
    private final UserInputReader userInputReader;
    private final Invoker invoker;
    private final IUserService userService;
    private final HabitService habitService;
    private final CommandContext commandContext;

    public AdminMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.invoker = commandContext.getInvoker();
        this.habitService = commandContext.getHabitService();
        this.userService = commandContext.getUserService();
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
                        Admin menu
                 ==============================
                1. Get all users
                2. Get all habits
                3. Delete user
                4. Block user
                5. Return to main menu

                Enter your choice:\s""");
    }

    void handleInput() {
        boolean exit = false;

        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    PrintUtil.printUserTable(userService.getAll());
                    openMenu(invoker, this);
                    yield true;
                }
                case "2" -> {
                    PrintUtil.printHabitTable(habitService.getAll());
                    openMenu(invoker, this);
                    yield true;
                }
                case "3" -> {
                    deleteUser();
                    openMenu(invoker, this);
                    yield true;
                }
                case "4" -> {
                    blockUser();
                    openMenu(invoker, this);
                    yield true;
                }
                case "5" -> {
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

    private void deleteUser() {
        Integer userId = getIdFromUserInput();
        userService.delete(userId);
        System.out.println("User with " + userId + " successfully deleted");
        System.out.println("\s\s\s");
    }

    private void blockUser() {
        Integer userId = getIdFromUserInput();
        userService.enable(userId, false);
        System.out.println("User with " + userId + " successfully blocked");
        System.out.println("\s\s\s");
    }

    private Integer getIdFromUserInput() {
        Integer userId = null;
        while (userId == null) {
            try {
                userId = Integer.parseInt(userInputReader.getUserInput("user id"));
            } catch (IllegalArgumentException | NotFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again...");
            }
        }
        return userId;
    }
}
