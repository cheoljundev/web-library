package com.weblibrary.domain.file.model;

import lombok.Data;
import lombok.Setter;

@Data
public class UploadFile {
    @Setter
    private Long uploadFileId;
    private final String uploadFileName;
    private final String storeFileName;
}
