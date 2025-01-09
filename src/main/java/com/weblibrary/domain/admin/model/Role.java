package com.weblibrary.domain.admin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Role {
    private final Long id;
    private final Long userId;
    private final RoleType roleType;
}
