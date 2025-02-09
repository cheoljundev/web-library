package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    /**
     * 회원 가입
     *
     * @param user
     * @return
     */
    User save(User user);

    /**
     * id로 회원 찾기
     * @param id : Long id
     * @return User
     */
    Optional<User> findById(Long id);

    /**
     * username으로 회원 찾기
     * @param username : String username
     * @return User
     */
    Optional<User> findByUsername(String username);

    /**
     * 모든 유저 반환
     *
     * @return List로 모든 유저를 반환
     */
    List<User> findAll(Number limit, Number offset);

    int countAll();

    /**
     * 유저 삭제
     *
     * @param userId : 삭제할 유저 id
     */
    void remove(Long userId);

    void update(User user);

}
