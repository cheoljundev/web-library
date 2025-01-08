package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryUserRepository implements UserRepository {

    @Getter
    private static final UserRepository instance = new MemoryUserRepository();

    private MemoryUserRepository() {
    }

    private final Map<Long, User> store = new HashMap<>();

    @Override
    public void save(User user) {
        store.put(user.getId(), user);
    }

    @Override
    public User findById(Long id) {
        return store.get(id);
    }

    @Override
    public User findByUsername(String username) {
        for (User user : store.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void clearAll() {
        store.clear();
    }
}
