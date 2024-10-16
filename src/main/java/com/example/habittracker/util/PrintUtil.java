package com.example.habittracker.util;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.dto.UserResponseTo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PrintUtil {

    public static void printUserTable(List<UserResponseTo> users) {
        printDelimiter();
        System.out.printf("%5s%15s%25s%15s%n", "ID", "NAME", "EMAIL", "STATUS");
        printDelimiter();

        for (UserResponseTo user : users) {
            System.out.format("%5s%15s%25s%15s%n", user.id(), user.name(), user.email(), user.isActive() ? "Active" : "Blocked");
        }

        printDelimiter();
        System.out.println("\s\s\s");
    }

    public static void printHabitTable(List<HabitTo> habits) {
        printDelimiter();
        if (habits.isEmpty()) {
            System.out.println("No habits have been added yet");
        } else
            System.out.printf("%5s%15s%25s%15s%n", "ID", "NAME", "FREQUENCY", "STATUS");
        printDelimiter();

        for (HabitTo habit : habits) {
            System.out.format("%5s%15s%25s%15s%n", habit.id(), habit.name(), habit.frequency().getDays() == 1 ? " Daily" : "Weekly", habit.isActive() ? "Active" : "Inactive");
        }

        if (!habits.isEmpty()) {
            printDelimiter();
        }
        System.out.println("\s\s\s");
    }

    private void printDelimiter() {
        System.out.println("==================================================================");
    }
}
