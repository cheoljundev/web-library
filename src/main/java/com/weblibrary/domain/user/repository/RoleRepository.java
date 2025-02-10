package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType);
    List<Role> findRolesByUserId(Long userId);
    void removeByRoleId(Long roleId);
}
