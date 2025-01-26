package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new HashMap<>();

    public static Long lastId = 0L;

    public static Long incrementLastId() {
        return ++lastId;
    }

    @Override
    public void save(User user) {
        store.put(user.getId(), user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
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
        return findById(userId)
                .map(user -> {
                    store.remove(userId);  // 사용자 제거
                    return user;  // 제거된 사용자 반환
                })
                .orElse(null);
    }

    @Override
    public void clearAll() {
        store.clear();
    }
}
