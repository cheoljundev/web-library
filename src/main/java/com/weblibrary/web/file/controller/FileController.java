package com.weblibrary.web.file.controller;

import com.weblibrary.domain.file.store.FileStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

/**
 * FileController는 파일 다운로드를 처리하는 REST 컨트롤러입니다.
 */
@Tag(name = "File API", description = "파일 다운로드 API")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;

    /**
     * 이미지 파일을 다운로드합니다.
     *
     * @param filename 파일명
     * @return 이미지 파일
     * @throws MalformedURLException 파일 URL이 잘못된 경우
     */
    @Operation(summary = "이미지 다운로드", description = "이미지 파일을 다운로드합니다.")
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable("filename") String filename) throws MalformedURLException {
        UrlResource urlResource = new UrlResource(fileStore.getUrlPath(filename));

        return ResponseEntity.ok()
                .contentType(getMediaType(filename))
                .body(urlResource);
    }

    /**
     * 파일명에 따라 MediaType을 결정합니다.
     *
     * @param filename 파일명
     * @return MediaType
     */
    private MediaType getMediaType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
            case "webp":
                return MediaType.IMAGE_JPEG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM; // 기본값
        }
    }
}
