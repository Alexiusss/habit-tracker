package com.example.habittracker.app;

import lombok.Getter;

import java.util.Scanner;

@Getter
public class UserInputReader {

    Scanner scanner = new Scanner(System.in);

    public String getUserChoice() {
        return scanner.next();
    }

    public String getUserInput(String string) {
        System.out.println("Enter " + string + ":");
        return getUserChoice();
    }

    public void close() {
        scanner.close();
    }
}
