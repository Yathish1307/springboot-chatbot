package com.university.bot.chatbot.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleDriveService {
    private static final String APPLICATION_NAME = "ChatbotApp";
    private static final String FOLDER_ID = "1vTVDaiN1iotd2Z0rh4PkFgX4szSXsUi2";
    private static final String SERVICE_ACCOUNT_JSON = "src/main/resources/drive-service-account.json";

    private Drive driveService;

    public GoogleDriveService() {
        try {
            GoogleCredential credential = GoogleCredential.fromStream(
                new FileInputStream(SERVICE_ACCOUNT_JSON))
                .createScoped(Collections.singleton("https://www.googleapis.com/auth/drive"));
            driveService = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Google Drive service", e);
        }
    }

    public List<File> listFilesInFolder() throws Exception {
        FileList result = driveService.files().list()
            .setQ("'" + FOLDER_ID + "' in parents and trashed = false")
            .setFields("files(id, name, mimeType)")
            .execute();
        return result.getFiles();
    }

    public InputStream downloadFile(String fileId) throws Exception {
        return driveService.files().get(fileId).executeMediaAsInputStream();
    }
} 