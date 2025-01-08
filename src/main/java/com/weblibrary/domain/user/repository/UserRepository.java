package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    void clearAll();
}
