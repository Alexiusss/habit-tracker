package com.example.habittracker.dto.in;

public record UserRequestTo (
        Integer id,
        String name,
        String email,
        String password
) {}