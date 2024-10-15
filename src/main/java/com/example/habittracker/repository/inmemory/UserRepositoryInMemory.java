package com.example.habittracker.repository.inmemory;

import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;
import com.example.habittracker.util.ValidationUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the {@code UserRepository} interface.
 * This class stores {@code User} entities in a {@code ConcurrentHashMap}.
 *
 * <p>It uses an atomic counter to generate unique IDs for new users.
 * All operations are performed in memory, so data will not persist across application restarts.</p>
 *
 * @author Alexey Boyarinov
 */
public class UserRepositoryInMemory extends BaseRepositoryInMemory implements UserRepository {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    /**
     * Saves or updates a user in the repository.
     * If the user is new, a unique ID is assigned.
     * If the user exists, it will be updated.
     *
     * @param user the user to save or update
     * @return the saved or updated user
     */
    @Override
    public User save(User user) {
        ValidationUtil.validate(user);
        if (user.isNew()) {
            user.setId(globalId.incrementAndGet());
            users.put(user.getId(), user);
            return user;
        } else {
            return users.computeIfPresent(user.getId(), (id, oldUser) -> user);
        }
    }

    /**
     * Deletes a user by ID from the repository.
     *
     * @param id the ID of the user to delete
     * @return {@code true} if the user was successfully deleted, {@code false} otherwise
     */
    @Override
    public boolean delete(int id) {
        return users.remove(id) != null;
    }

    /**
     * Retrieves a user by ID from the repository.
     *
     * @param id the ID of the user to retrieve
     * @return the user with the specified ID, or {@code null} if not found
     */
    @Override
    public User get(int id) {
        return users.get(id);
    }

    /**
     * Retrieves a user by their email address from the repository.
     *
     * @param email the email address of the user to retrieve
     * @return the user with the specified email, or {@code null} if not found
     */
    @Override
    public User getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all users stored in the repository.
     *
     * @return a list of all users
     */
    @Override
    public List<User> getAll() {
        return users.values().stream()
                .toList();
    }
}