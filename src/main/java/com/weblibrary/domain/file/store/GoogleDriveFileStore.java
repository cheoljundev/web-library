package com.weblibrary.domain.file.store;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.weblibrary.domain.file.model.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
//@Service
public class GoogleDriveFileStore implements FileStore {

    private static final String APPLICATION_NAME = "Google Drive API Java";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    // credentials.json 파일을 클래스패스에서 읽도록 경로를 수정합니다.
    private static final String CREDENTIALS_RESOURCE_PATH = "/credentials.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private final Drive driveService;

    public GoogleDriveFileStore() {
        try {
            this.driveService = getDriveService();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Google Drive service", e);
        }
    }

    private Drive getDriveService() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(httpTransport);
        return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        InputStream in = getClass().getResourceAsStream(CREDENTIALS_RESOURCE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_RESOURCE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, Collections.singletonList(DriveScopes.DRIVE_FILE))
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Override
    public String getUrlPath(String filename) {
        String name = filename.split("\\.")[0];
        return "https://drive.google.com/uc?export=view&id=" + name;
    }

    @Override
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }

    @Override
    public UploadFile storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String generatedFileName = FileStore.createStoreFileName(originalFilename);

        try {
            File tempFile = convertMultipartFileToFile(multipartFile);
            // 업로드 후 반환되는 fileId를 얻습니다.
            String fileId = uploadToGoogleDrive(tempFile, generatedFileName);
            tempFile.delete();
            log.debug("File uploaded to Google Drive with ID: {}", fileId);
            // 여기서 fileId를 storeFileName으로 사용하여 UploadFile 객체를 생성합니다.
            return new UploadFile(originalFilename, fileId);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("upload_", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

    private String uploadToGoogleDrive(File localFile, String fileName) throws IOException {
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(fileName);

        FileContent mediaContent = new FileContent("application/octet-stream", localFile);
        com.google.api.services.drive.model.File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        com.google.api.services.drive.model.Permission permission = new com.google.api.services.drive.model.Permission()
                .setType("anyone")
                .setRole("reader");

        driveService.permissions().create(uploadedFile.getId(), permission).execute();

        return uploadedFile.getId();
    }

    @Override
    public void deleteFile(String storeFileName) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    driveService.files().delete(storeFileName).execute();
                    log.debug("File deleted from Google Drive with ID: {}", storeFileName);
                } catch (IOException e) {
                    log.error("Failed to delete file from Google Drive", e);
                }
            }
        });
    }
}