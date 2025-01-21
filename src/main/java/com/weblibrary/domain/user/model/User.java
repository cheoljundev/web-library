package com.weblibrary.domain.user.model;

import com.weblibrary.domain.book.model.Book;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@RequiredArgsConstructor
@Getter @ToString
public class User {
    private final Long id;
    private final String username;
    private final String password;
    private int remainingRents = 3; // 남은 대출 가능 권수

    /**
     * 책 대출 메서드
     * 책을 대출, 대출 권수 차감
     *
     * @param book : 빌릴 책
     */
    public void rent(Book book) {
        book.rent(this);
        remainingRents--;
    }

    /**
     * 책 반납 메서드
     * 책을 반납, 대출 권수를 복구한다.
     *
     * @param book : 반납할 책
     */
    public void unRent(Book book) {
        book.unRent(this);
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
