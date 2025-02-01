package com.weblibrary.domain.admin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Role implements Comparable<Role>{
    private final Long roleId;
    private final Long userId;
    private final RoleType roleType;

    @Override
    public int compareTo(Role other) {
        return roleType.compareTo(other.roleType);
    }
}
