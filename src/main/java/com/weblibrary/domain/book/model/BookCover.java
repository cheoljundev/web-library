package com.weblibrary.domain.book.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "book_covers")
public class BookCover {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookCoverId;
    private final Long bookId;
    @Setter
    private Long uploadFileId;

    public BookCover(Long bookId, Long uploadFileId) {
        this.bookId = bookId;
        this.uploadFileId = uploadFileId;
    }
}
