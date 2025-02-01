package com.weblibrary.domain.admin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Role implements Comparable<Role>{
    @Setter
    private Long roleId;
    private final Long userId;
    private final RoleType roleType;

    @Override
    public int compareTo(Role other) {
        return roleType.compareTo(other.roleType);
    }
}
