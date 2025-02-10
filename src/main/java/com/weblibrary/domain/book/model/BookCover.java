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
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookCoverId;
    private final Long bookId;
    private final Long uploadFileId;
}
