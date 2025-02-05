package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;

public interface DbUserRepository {
    /**
     * 유저 업데이트
     * @param user
     * @return oldUser
     */
    User update(User user);
}
