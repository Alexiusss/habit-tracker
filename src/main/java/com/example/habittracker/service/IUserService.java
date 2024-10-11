package com.example.habittracker.service;

import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;

import java.util.List;

public interface IUserService {
    UserResponseTo get(int id);

    UserResponseTo getByEmail(String email);

    List<UserResponseTo> getAll();

    UserResponseTo create(UserRequestTo user);

    void update(UserRequestTo user);

    void delete(int id);
}