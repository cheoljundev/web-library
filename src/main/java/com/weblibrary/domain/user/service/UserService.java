package com.weblibrary.domain.user.service;

import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.DbUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final DbUserRepository dbUserRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * username으로 유저를 찾음
     *
     * @param username : String
     * @return : User
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User update(User user) {
        if (userRepository instanceof DbUserRepository repository) {
            return repository.update(user);
        }

        return null;
    }


}
