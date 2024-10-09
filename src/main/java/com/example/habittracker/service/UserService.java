package com.example.habittracker.service;

import com.example.habittracker.dto.in.UserRequestTo;
import com.example.habittracker.dto.out.UserResponseTo;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseTo get(int id) {
        return null;
    }

    @Override
    public UserResponseTo getByEmail(String email) {
        return null;
    }

    @Override
    public List<UserResponseTo> getAll() {
        return null;
    }

    @Override
    public UserResponseTo create(UserRequestTo userTO) {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(int id) {

    }
}