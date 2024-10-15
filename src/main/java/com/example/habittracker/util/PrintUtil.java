package com.example.habittracker.util;

import com.example.habittracker.dto.HabitTo;
import com.example.habittracker.dto.UserResponseTo;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PrintUtil {

    public static void printUserTable(List<UserResponseTo> users) {
        System.out.println("==================================================");
            System.out.printf("%5s %10s %12s %10s", "ID", "NAME", "EMAIL", "STATUS");
        System.out.println();
        System.out.println("==================================================");

        for (UserResponseTo user : users) {
            System.out.format("%5s %12s %14s %10s", user.id(), user.name(), user.email(), user.isActive() ? "Active" : "Blocked");
            System.out.println();
        }
        System.out.println("==================================================");
        System.out.println("\s\s\s");
    }

    public static void printHabitTable(List<HabitTo> habits) {
        System.out.println("==================================================");
        if (habits.isEmpty()) {
            System.out.println("No habits have been added yet");
        } else
            System.out.printf("%5s %10s %12s %10s", "ID", "NAME", "FREQUENCY", "STATUS");
        System.out.println();
        System.out.println("==================================================");


        for (HabitTo habit : habits) {
            System.out.format("%5s %12s %14s %10s", habit.id(), habit.name(), habit.frequency().getDays() == 1 ? " Daily" : "Weekly", habit.isActive() ? "Active" : "Inactive");
            System.out.println();
        }
        if (!habits.isEmpty()) {
            System.out.println("==================================================");
        }
        System.out.println("\s\s\s");
    }
}
