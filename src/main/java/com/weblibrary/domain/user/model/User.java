package com.weblibrary.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class User {
    private final Long id;
    private final String username;
    private final String password;
}
