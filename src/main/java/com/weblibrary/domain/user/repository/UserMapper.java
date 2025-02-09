package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    /**
     * 회원 가입
     *
     * @param user
     * @return
     */
    void save(User user);

    /**
     * id로 회원 찾기
     *
     * @param userId : Long id
     * @return User
     */
    Optional<User> findById(Long userId);

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
    List<User> findAll(@Param("limit") Number limit, @Param("offset") Number offset);

    int countAll();

    void update(User user);

    /**
     * 유저 삭제
     *
     * @param userId : 삭제할 유저 id
     */
    void remove(Long userId);

}
