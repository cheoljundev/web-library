package com.weblibrary.domain.book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class BookCover {
    @EqualsAndHashCode.Include
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookCoverId;
    private final Long bookId;
    private final Long uploadFileId;
}
