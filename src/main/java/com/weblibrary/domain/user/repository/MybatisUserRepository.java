package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisUserRepository implements UserRepository {

    private final UserMapper mapper;

    @Override
    public User save(User user) {
        mapper.save(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return mapper.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return mapper.findByUsername(username);
    }

    @Override
    public List<User> findAll(Number limit, Number offset) {
        return mapper.findAll(limit, offset);
    }

    @Override
    public int countAll() {
        return mapper.countAll();
    }

    @Override
    public void update(User user) {
        mapper.update(user);
    }

    @Override
    public void remove(Long userId) {
        mapper.remove(userId);
    }
}
