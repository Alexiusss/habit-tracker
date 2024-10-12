package com.example.habittracker.util;

import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.exception.PasswordNotValidException;
import com.example.habittracker.model.Role;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.service.IUserService;

import java.util.Set;

import static com.example.habittracker.util.UserUtil.asTo;
import static com.example.habittracker.util.ValidationUtil.validateEmail;


public class SecurityUtil {

    private User currentUser;

    private final UserRepository userRepository;
    private final IUserService userService;

    public SecurityUtil(UserRepository userRepository, IUserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        userRepository.save(new User("admin@gmail.com", "admin777", true, Set.of(Role.ADMIN, Role.USER)));
    }

    public boolean login(String email, String password) {
        validateEmail(email);
        User user = userRepository.getByEmail(email);
        if (user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        } else {
            throw new PasswordNotValidException("Password is incorrect");
        }
    }

    public void deleteAccount() {
        userService.delete(currentUser.getId());
        currentUser = null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRoles().contains(Role.ADMIN);
    }

    public UserResponseTo getCurrentUserProfile() {
        return asTo(currentUser);
    }
}