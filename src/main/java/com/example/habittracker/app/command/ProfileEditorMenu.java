package com.example.habittracker.app.command;

import com.example.habittracker.app.UserInputReader;
import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.exception.DuplicateEmailException;
import com.example.habittracker.exception.EmailNotValidException;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.service.IUserService;
import com.example.habittracker.util.SecurityUtil;

public class ProfileEditorMenu implements Command {
    private final UserInputReader userInputReader;
    private final SecurityUtil securityUtil;
    private final IUserService userService;
    private final Invoker invoker;
    private final CommandContext commandContext;

    public ProfileEditorMenu(CommandContext commandContext) {
        this.userInputReader = commandContext.getUserInputReader();
        this.securityUtil = commandContext.getSecurityUtil();
        this.userService = commandContext.getUserService();
        this.invoker = commandContext.getInvoker();
        this.commandContext = commandContext;
    }

    @Override
    public void execute() {
        UserResponseTo profile = securityUtil.getCurrentUserProfile();

        printEditor(profile);
        updateProfile(profile);

        UserResponseTo updatedUser = userService.get(profile.id());
        printEditor(updatedUser);

        invoker.setCommand(new MainMenu((commandContext)));
        invoker.executeCommand();
    }

    private void printEditor(UserResponseTo profile) {
        System.out.println("======================");
        System.out.println("    Profile editor    ");
        System.out.println("======================");
        System.out.println("Email: " + profile.email());
        System.out.println("Name: " + profile.name());
    }

    private void updateProfile(UserResponseTo profile) {
        boolean isSaved = false;
        while (!isSaved) {
            String email = userInputReader.getUserInput("email");
            String name = userInputReader.getUserInput("name");
            String password = userInputReader.getUserInput("password");
            try {
                userService.update(new UserRequestTo(profile.id(), name, email, password));
            } catch (EmailNotValidException | PasswordNotValidException | DuplicateEmailException e) {
                System.out.println(e.getMessage());
            }
            isSaved = true;
            System.out.println("You profile successfully updated");
        }
    }
}
