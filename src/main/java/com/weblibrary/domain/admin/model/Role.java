package com.weblibrary.domain.admin.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role implements Comparable<Role>{
    @Setter
    @EqualsAndHashCode.Include
    private Long roleId;
    private final Long userId;
    private final RoleType roleType;

    @Override
    public int compareTo(Role other) {
        return roleType.compareTo(other.roleType);
    }
}
