package com.weblibrary.domain.file.model;

import lombok.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UploadFile {
    @EqualsAndHashCode.Include
    @Setter
    private Long uploadFileId;
    private final String uploadFileName;
    private final String storeFileName;
}
