package com.example.habittracker;


import java.util.Scanner;

public class HabitTrackerApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        printWelcomeMenu();

        while (!exit) {
            int userChoice = getUserChoice();

            exit = switch (userChoice) {
                case 1 -> {
                    handleLoginMenu();
                    yield false;
                }
                case 2 -> {
                    handleRegisterMenu();
                    yield false;
                }
                case 3 -> {
                    System.out.println("Exiting the program...");
                    yield true;
                }
                default -> {
                    System.out.println("Invalid choice. Please try again.");
                    yield false;
                }
            };
        }
        scanner.close();
    }

    private static void printWelcomeMenu() {
        System.out.println("""
                --- \nWelcome to habit tracker app ---
                1. Sign in
                2. Register
                3. Exit
                Enter your choice:\s""");
    }

    private static void handleLoginMenu() {
        System.out.println("Log in menu");
        String email = getUserInput("email");
        String password = getUserInput("password");

        handleMainMenu();
    }


    private static void handleRegisterMenu() {
        System.out.println("--- Register menu ---");
        String email = getUserInput("email");
        String password = getUserInput("password");

        System.out.println("User successfully registered. Sign in with your email and password.\n\n\n");
        printWelcomeMenu();
    }

    private static void handleMainMenu() {
        System.out.println("""
                --- Main menu ---
                1. Habits
                2. Profile
                3. Exit
                Enter your choice:\s""");
    }

    private static int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static String getUserInput(String string) {
        System.out.println("Enter your " + string + ":");
        return scanner.next();
    }
}