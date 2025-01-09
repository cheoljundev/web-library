package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.User;

import java.util.List;

public interface UserRepository {
    /**
     * 회원 가입
     * @param user
     */
    void save(User user);

    /**
     * id로 회원 찾기
     * @param id : Long id
     * @return User
     */
    User findById(Long id);

    /**
     * username으로 회원 찾기
     * @param username : String username
     * @return User
     */
    User findByUsername(String username);

    /**
     * 모든 유저 반환
     * @return List로 모든 유저를 반환
     */
    List<User> findAll();

    void setRole(User user, Role role);

    /**
     * 유저 삭제
     * @param user : 삭제할 유저 객체
     * @return 삭제한 유저 반환
     */
    User remove(User user);

    /**
     * 저장소 비우기
     */
    void clearAll();
}
