package com.weblibrary.domain.file.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor(force = true)
public class UploadFile {
    @EqualsAndHashCode.Include
    @Setter
    @Id
    private Long uploadFileId;
    private final String uploadFileName;
    private final String storeFileName;
}
