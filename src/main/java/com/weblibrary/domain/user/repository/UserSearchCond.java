package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCond {
    private String username;
    private RoleType roleType;
}
