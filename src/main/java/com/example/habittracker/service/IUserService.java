package com.example.habittracker.service;

import com.example.habittracker.dto.in.UserRequestTo;
import com.example.habittracker.dto.out.UserResponseTo;
import com.example.habittracker.model.User;

import java.util.List;

public interface IUserService {
    UserResponseTo get(int id);

    UserResponseTo getByEmail(String email);

    List<UserResponseTo> getAll();

    UserResponseTo create(UserRequestTo user);

    void update (User user);

    void delete(int id);
}