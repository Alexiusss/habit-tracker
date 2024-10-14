package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.exception.EmailNotValidException;
import com.example.habittracker.exception.NotFoundException;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.util.SecurityUtil;

import static com.example.habittracker.util.ConsoleUtil.openMenu;

public class LoginMenu implements Command {

    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final Invoker invoker;
    private final CommandContext commandContext;

    public LoginMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.invoker = commandContext.getInvoker();
        this.commandContext = commandContext;
    }

    @Override
    public void execute() {
        System.out.println("=====================");
        System.out.println("     Login menu      ");
        System.out.println("=====================");
        boolean isAuth = false;

        while (!isAuth) {
            String email = userInputReader.getUserInput("email");
            String password = userInputReader.getUserInput("password");
            System.out.println("\n");
            try {
                isAuth = securityUtil.login(email, password);
            } catch (NotFoundException | PasswordNotValidException | EmailNotValidException e) {
                System.out.println(e.getMessage());
            }
        }
        openMenu(invoker, new MainMenu(commandContext));
    }
}
