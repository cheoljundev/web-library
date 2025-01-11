package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();

    public static Long lastId = 0L;

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
    public User remove(Long userId) {
        User removed = findById(userId);
        store.remove(userId);
        return removed;
    }

    @Override
    public void clearAll() {
        store.clear();
    }
}
