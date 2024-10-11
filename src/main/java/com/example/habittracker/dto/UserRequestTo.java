package com.example.habittracker.dto;

public record UserRequestTo (
        Integer id,
        String name,
        String email,
        String password
) {}