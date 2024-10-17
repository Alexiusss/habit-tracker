package com.example.habittracker.app;

import com.example.habittracker.app.command.CommandContext;
import com.example.habittracker.app.command.StartMenu;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConsoleApp {

    private final CommandContext commandContext;

    public void start() {
        StartMenu startMenu = new StartMenu(commandContext);
        startMenu.execute();
    }
}
