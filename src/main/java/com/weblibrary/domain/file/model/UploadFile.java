package com.weblibrary.domain.file.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor(force = true)
@Table(name = "upload_files")
public class UploadFile {
    @EqualsAndHashCode.Include
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uploadFileId;
    private final String uploadFileName;
    private final String storeFileName;
}
