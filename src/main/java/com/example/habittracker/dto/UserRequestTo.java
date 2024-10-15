package com.example.habittracker.dto;

/**
 * A transfer object (DTO) that represents a request for user creation.
 *
 * @author Alexey Boyarinov
 */
public record UserRequestTo (
        Integer id,
        String name,
        String email,
        String password
) {}