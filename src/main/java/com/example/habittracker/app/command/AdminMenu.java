package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.service.UserService;
import com.example.habittracker.service.impl.HabitServiceImpl;

import static com.example.habittracker.util.ConsoleUtil.openMenu;
import static com.example.habittracker.util.print.MenuPrinterUtil.printAdminMenu;
import static com.example.habittracker.util.print.TablePrinterUtil.TablePrinter.printHabitTable;
import static com.example.habittracker.util.print.TablePrinterUtil.TablePrinter.printUserTable;

public class AdminMenu implements Command {
    private final UserInputReader userInputReader;
    private final Invoker invoker;
    private final UserService userService;
    private final HabitServiceImpl habitService;
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
        printAdminMenu();
        handleInput();
    }

    void handleInput() {
        boolean exit = false;

        while (!exit) {
            String choice = userInputReader.getUserChoice();
            exit = switch (choice) {
                case "1" -> {
                    printUserTable(userService.getAll());
                    openMenu(invoker, this);
                    yield true;
                }
                case "2" -> {
                    printHabitTable(habitService.getAll());
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
