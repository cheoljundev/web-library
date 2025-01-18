package com.weblibrary.domain.admin.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 유저의 권한을 설정하는 Enum Class
 * Default: 일반 유저
 * Admin: 관리자 유저
 */
@Getter
@RequiredArgsConstructor @ToString
public enum RoleType {
    ADMIN("관리자"), DEFAULT("일반 유저");

    private final String description;
}
