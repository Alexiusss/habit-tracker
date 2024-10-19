package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.exception.DuplicateEmailException;
import com.example.habittracker.exception.EmailNotValidException;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.service.UserService;
import com.example.habittracker.util.SecurityUtil;

import java.sql.SQLException;

import static com.example.habittracker.util.ConsoleUtil.openMenu;
import static com.example.habittracker.util.print.MenuPrinterUtil.printProfileEditorMenu;

public class ProfileEditorMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final Invoker invoker;

    public ProfileEditorMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.userService = commandContext.getUserService();
        this.invoker = commandContext.getInvoker();
    }

    @Override
    public void execute() {
        UserResponseTo profile = securityUtil.getCurrentUserProfile();
        printProfileEditorMenu(profile);
        handleProfileUpdate(profile);
    }

    private void printEditor(UserResponseTo profile) {

    }

    private void handleProfileUpdate(UserResponseTo profile) {
        boolean isSaved = false;
        while (!isSaved) {
            String email = userInputReader.getUserInput("email");
            String name = userInputReader.getUserInput("name");
            String password = userInputReader.getUserInput("password");
            try {
                userService.update(new UserRequestTo(profile.id(), name, email, password));
                isSaved = true;
                System.out.println("You profile successfully updated");
            } catch (EmailNotValidException | PasswordNotValidException | DuplicateEmailException e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        openMenu(invoker, this);
    }
}
