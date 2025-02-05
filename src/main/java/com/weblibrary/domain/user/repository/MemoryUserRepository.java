package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;

import java.util.*;

public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();

    public static Long lastId = 0L;

    public static Long incrementLastId() {
        return ++lastId;
    }

    @Override
    public User save(User user) {
        user.setUserId(incrementLastId());
        store.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<User> remove(Long userId) {
        return Optional.ofNullable(store.remove(userId));
    }

    public void clearAll() {
        store.clear();
    }
}
