package com.example.habittracker.util.print;

import com.example.habittracker.dto.UserResponseTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MenuPrinterUtil {
    public static void printStartMenu() {
        printMenuHeader("Habit tracker app");
        System.out.println("""
                1. Sign in
                2. Register

                Type exit or q to quit

                Enter your choice:\s""");
    }

    public static void printMainMenu(boolean isAdmin) {
        printMenuHeader("Main menu");
        System.out.print("""
                1. Habits
                2. Profile
                """);
        if (isAdmin) {
            System.out.println("""
                    3. Admin panel
                    """);
        }
        System.out.println("""                
                Type exit or q to quit
                                 
                Enter your choice:\s
                """);
    }

    public static void printAdminMenu() {
        printMenuHeader("Admin menu");
        System.out.println("""
                 1. Get all users
                 2. Get all habits
                 3. Delete user
                 4. Block user
                 5. Return to main menu

                 Enter your choice:\s""");
    }

    public static void printProfileMenuEditor() {
        printMenuHeader("Profile");
        System.out.println("""
                1. Profile editor
                2. Delete account
                3. Return to main menu

                Enter your choice:\s""");
    }

    public static void printStatisticsAndAnalyticsMenu() {
        printMenuHeader("Statistics and analytic");
        System.out.print("""
                 1. Get count of current series of execution(streak)
                 2. Get success statistics for period
                 3. Back to main menu
                 
                Enter your choice:\s
                """);
    }

    public static void printProfileEditorMenu(UserResponseTo profile) {
        printMenuHeader("Profile editor");
        System.out.println("Email: " + profile.email());
        System.out.println("Name: " + profile.name());
        System.out.println("\s\s\s");
    }

    public static void printHabitListMenuSelect() {
        System.out.println("""
                1. Set filter
                2. Mark habit performed
                3. Return to habit menu

                Enter your choice:\s""");
    }

    public static void printHabitMenu() {
        printMenuHeader("Habits menu");
        System.out.println("""
                1. Create
                2. Edit
                3. Delete
                4. Get filtered list
                5. Statistics and analytics
                6. Return to main menu

                Enter your choice:\s""");
    }
    public static void printMenuHeader(String title) {
        System.out.println("==================================");
        System.out.println("        " + title);
        System.out.println("==================================");
    }
}
