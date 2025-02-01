package com.weblibrary.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
@ToString
public class User {
    @Setter
    private Long userId;
    private final String username;
    private final String password;
    private int remainingRents = 3; // 남은 대출 가능 권수

    public void decrementRemainingRents() {
        remainingRents--;
    }

    public void incrementRemainingRents() {
        remainingRents++;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(getUserId(), user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId());
    }
}
