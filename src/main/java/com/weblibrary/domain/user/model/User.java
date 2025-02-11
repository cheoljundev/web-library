package com.weblibrary.domain.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Setter
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private int remainingRents = 3; // 남은 대출 가능 권수

    public User(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.remainingRents = user.getRemainingRents();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
