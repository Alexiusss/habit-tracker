package com.example.habittracker.service;

import java.util.List;

import com.example.habittracker.exception.NotFoundException;
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

/**
 * Service implementation for managing users.
 * Provides CRUD methods.
 *
 * @author Alexey Boyarinov
 */
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;

    /**
     * Retrieves a user by its ID.
     *
     * @param id the user ID to retrieve
     * @return the {@code UserResponseTo} representing the found user
     * @throws NotFoundException if the user with the specified ID does not exist
     */
    @Override
    public UserResponseTo get(int id) {
        User user = checkNotFoundWithId(repository.get(id), id);
        return asTo(user);
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the user's email
     * @return the {@code UserResponseTo} representing the found user
     * @throws NotFoundException if no user is found with the specified email
     */
    @Override
    public UserResponseTo getByEmail(String email) {
        User user = checkNotFound(repository.getByEmail(email), email);
        return asTo(user);
    }


    /**
     * Retrieves all users.
     *
     * @return a list of {@code UserResponseTo} objects representing all users
     */
    @Override
    public List<UserResponseTo> getAll() {
        return repository.getAll().stream()
                .map(UserUtil::asTo)
                .toList();
    }

    /**
     * Creates a new user from the given {@code UserRequestTo}.
     * Validates the input and checks if the email is already used by another user.
     *
     * @param userTO the {@code UserRequestTo} containing user details
     * @return the {@code UserResponseTo} representing the created user
     * @throws DuplicateEmailException if a user with the specified email already exists
     */
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

    /**
     * Updates an existing user with the given details.
     * The user is retrieved by ID, and then updated with the data in {@code UserRequestTo}.
     *
     * @param userTo the {@code UserRequestTo} containing updated user details
     * @throws NotFoundException if the user with the specified ID is not found
     */
    @Override
    public void update(UserRequestTo userTo) {
        assertNotNull(userTo, "user must not be null");
        User user = repository.get(userTo.id());
        User updateUser = updateFromTo(user, userTo);
        repository.save(updateUser);
    }

    /**
     * Deletes a user by its ID.
     * Throws a {@code NotFoundException} if the user is not found.
     *
     * @param id the user ID to delete
     * @throws NotFoundException if the user with the specified ID is not found
     */
    @Override
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    /**
     * Activate or block a user account by setting its active status.
     *
     * @param id the user ID to modify
     * @param enabled {@code true} to activate, {@code false} to block
     * @throws NotFoundException if the user with the specified ID is not found
     */
    @Override
    public void enable(int id, boolean enabled) {
        User user = repository.get(id);
        user.setActive(false);
        repository.save(user);
    }
}