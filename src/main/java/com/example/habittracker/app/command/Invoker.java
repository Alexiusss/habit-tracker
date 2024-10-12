package com.example.habittracker.app.command;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class Invoker {
    private Command command;
    public void executeCommand() {
        command.execute();
    }
}
