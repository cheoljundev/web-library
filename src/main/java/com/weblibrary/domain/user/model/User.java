package com.weblibrary.domain.user.model;

import com.weblibrary.domain.book.model.Book;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class User {
    private final Long id;
    private final String username;
    private final String password;
    private int remainingRents = 3; // 남은 대출 가능 권수

    /**
     * 책 대출 메서드, 남은 권수가 0권 이상이면, book.rent 메서드 호출해서 결과 리턴
     * @param book : 빌릴 책
     * @return 성공 여부
     */
    public boolean rent(Book book) {
        if (remainingRents > 0) {
            remainingRents--;

            return book.rent(this);
        }

        return false;
    }

    /**
     * 책 반납 메서드, book.unRent 메서드 호출해서 성공하면, 대출 가능 권수 늘리고 성공 리턴
     * @param book : 반납할 책
     * @return : 성공 여부
     */
    public boolean unRent(Book book) {
        if (book.unRent(this)) {
            remainingRents++;
            return true;
        }
        return false;
    }
}
