package com.weblibrary.domain.user.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class UserInfo {
    private final Long id;
    private final String username;
    private final List<RoleTypeInfo> roles;
    private final int remainingRents;
}
