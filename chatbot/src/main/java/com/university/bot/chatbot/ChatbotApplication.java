package com.university.bot.chatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.university.bot.chatbot.service.GoogleDriveService;
import com.google.api.services.drive.model.File;
import java.util.List;

@SpringBootApplication
public class ChatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatbotApplication.class, args);
    }

    @Bean
    public CommandLineRunner testGoogleDrive(GoogleDriveService googleDriveService) {
        return args -> {
            try {
                List<File> files = googleDriveService.listFilesInFolder();
                System.out.println("Files in Google Drive folder:");
                for (File file : files) {
                    System.out.println("Name: " + file.getName() + ", ID: " + file.getId());
                }
            } catch (Exception e) {
                System.err.println("Error accessing Google Drive: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
} 