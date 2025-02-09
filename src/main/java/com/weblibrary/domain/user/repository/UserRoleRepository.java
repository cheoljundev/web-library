package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Mapper
@Repository
public interface UserRoleRepository {
    void save(Role role);
    Optional<Role> findById(Long roleId);
    Optional<Role> findRoleByUserIdAndRoleType(@Param("userId") Long userId, @Param("roleType") RoleType roleType);
    List<Role> findRolesByUserId(Long userId);
    int remove(Long roleId);
}
