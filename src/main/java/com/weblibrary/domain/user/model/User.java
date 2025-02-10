package com.weblibrary.domain.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@NoArgsConstructor(force = true)
public class User {
    @Setter
    @EqualsAndHashCode.Include
    @Id
    private Long userId;
    private final String username;
    private final String password;
    private int remainingRents = 3; // 남은 대출 가능 권수

    public User(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.remainingRents = user.getRemainingRents();
    }

    public void rentBook(){
        decrementRemainingRents();
    }

    public void returnBook(){
        incrementRemainingRents();
    }

    private void decrementRemainingRents() {
        remainingRents--;
    }

    private void incrementRemainingRents() {
        remainingRents++;
    }
}
