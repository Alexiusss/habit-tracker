package com.example.habittracker.service;

import java.util.List;

import com.example.habittracker.util.UserUtil;
import lombok.AllArgsConstructor;
import com.example.habittracker.dto.UserRequestTo;
import com.example.habittracker.dto.UserResponseTo;
import com.example.habittracker.exception.DuplicateEmailException;
import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;

import static com.example.habittracker.util.UserUtil.*;
import static com.example.habittracker.util.ValidationUtil.checkNotFoundWithId;
import static com.example.habittracker.util.ValidationUtil.assertNotNull;
import static com.example.habittracker.util.ValidationUtil.checkNotFound;


@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;

    @Override
    public UserResponseTo get(int id) {
        User user = checkNotFoundWithId(repository.get(id), id);
        return asTo(user);
    }


    @Override
    public UserResponseTo getByEmail(String email) {
        User user = checkNotFound(repository.getByEmail(email), email);
        return asTo(user);
    }

    @Override
    public List<UserResponseTo> getAll() {
        return repository.getAll().stream()
                .map(UserUtil::asTo)
                .toList();
    }

    @Override
    public UserResponseTo create(UserRequestTo userTO) {
        assertNotNull(userTO, "user must not be null");
        if (repository.getByEmail(userTO.email()) != null) {
            throw new DuplicateEmailException("User with email " + userTO.email() + " already exists");
        }
        User user = createNewFromTo(userTO);
        User savedUser = repository.save(user);
        return asTo(savedUser);
    }

    @Override
    public void update(UserRequestTo userTo) {
        assertNotNull(userTo, "user must not be null");
        User user = repository.get(userTo.id());
        User updateUser = updateFromTo(user, userTo);
        repository.save(updateUser);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public void enable(int id, boolean enabled) {
        User user = repository.get(id);
        user.setActive(false);
        repository.save(user);
    }
}