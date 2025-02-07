package com.weblibrary.domain.admin.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class UserInfo {
    private final Long id;
    private final String username;
    private final String roleTypeName;
}
