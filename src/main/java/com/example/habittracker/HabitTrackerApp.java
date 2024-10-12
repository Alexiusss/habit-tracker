package com.example.habittracker;


import com.example.habittracker.app.command.CommandContext;
import com.example.habittracker.app.ConsoleApp;

public class HabitTrackerApp {
    public static void main(String[] args) {
        ConsoleApp app = new ConsoleApp(new CommandContext());
        app.start();
    }
}