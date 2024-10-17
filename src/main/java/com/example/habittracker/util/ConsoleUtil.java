package com.example.habittracker.util;

import com.example.habittracker.app.command.Command;
import com.example.habittracker.app.command.Invoker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConsoleUtil {
    public static void openMenu(Invoker invoker, Command command) {
        invoker.setCommand(command);
        invoker.executeCommand();
    }
}