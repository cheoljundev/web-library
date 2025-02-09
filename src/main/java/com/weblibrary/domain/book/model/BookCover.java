package com.weblibrary.domain.book.model;

import lombok.*;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BookCover {
    @EqualsAndHashCode.Include
    @Setter
    private Long bookCoverId;
    private final Long bookId;
    private final Long uploadFileId;
}
