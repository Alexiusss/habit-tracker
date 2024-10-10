package com.example.habittracker.util;

import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.service.IUserService;

import static com.example.habittracker.util.ValidationUtil.validateEmail;


public class SecurityUtil {

    private User currentUser;

    private final UserRepository userRepository;
    private final IUserService userService;

    public SecurityUtil(UserRepository userRepository, IUserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public void login(String email, String password) {
        validateEmail(email);
        User user = userRepository.getByEmail(email);
        if (user.getPassword().equals(password)) {
            currentUser = user;
        } else {
            throw new PasswordNotValidException("Password is incorrect");
        }
    }

    public void deleteAccount(Integer id) {
        userService.delete(id);
        currentUser = null;
    }
}