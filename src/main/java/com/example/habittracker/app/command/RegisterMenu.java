package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.exception.DuplicateEmailException;
import com.example.habittracker.exception.EmailNotValidException;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.example.habittracker.util.ConsoleUtil.openMenu;
import static com.example.habittracker.util.print.MenuPrinterUtil.printMenuHeader;

@AllArgsConstructor
public class RegisterMenu implements Command {

    private final UserInputReader userInputReader;
    private final UserService userService;
    private final Invoker invoker;

    @Getter
    private final CommandContext commandContext;

    public RegisterMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.userService = commandContext.getUserService();
        this.invoker = commandContext.getInvoker();
        this.commandContext = commandContext;
    }


    @Override
    public void execute() {
        printMenuHeader("Register menu");
        handleInput();
    }

    private void handleInput() {
        UserResponseTo user = null;
        while (user == null) {
            String email = userInputReader.getUserInput("email");
            String password = userInputReader.getUserInput("password");
            try {
                user = userService.create(new UserRequestTo(null, null, email, password));
            } catch (EmailNotValidException | PasswordNotValidException | DuplicateEmailException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("User successfully registered. Sign in with your email and password.\n\n\n");

        openMenu(invoker, new LoginMenu(commandContext));
    }
}
