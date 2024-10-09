package com.example.habittracker.repository.inmemory;

import com.example.habittracker.model.User;
import com.example.habittracker.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepositoryInMemory extends BaseRepositoryInMemory implements UserRepository {

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        user.setId(globalId.incrementAndGet());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public boolean delete(int id) {
        return users.remove(id) != null;
    }

    @Override
    public User get(int id) {
        return users.get(id);
    }

    @Override
    public User getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getAll() {
        return users.values().stream()
                .toList();
    }
}