package com.weblibrary.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class SetUserDto {
    private final Long id;
    private final String username;
    private final String roleTypeName;
}
