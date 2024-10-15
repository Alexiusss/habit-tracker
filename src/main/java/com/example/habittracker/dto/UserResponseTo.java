package com.example.habittracker.dto;

/**
 * A data transfer object (DTO) representing the response for a user.
 * This class encapsulates the basic details of a user when a response is provided.
 *
 * @author Alexey Boyarinov
 */
public record UserResponseTo(
        Integer id,
        String name,
        String email,
        boolean isActive
)
{}