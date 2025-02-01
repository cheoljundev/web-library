package com.weblibrary.domain.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
@ToString
public class User {
    private final Long id;
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
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
