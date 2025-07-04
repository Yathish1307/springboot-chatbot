package com.university.bot.chatbot.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.google.api.services.drive.model.File;

@Service
public class QnAService {
    private final GoogleDriveService googleDriveService;
    private final DocumentParserService documentParserService;
    @Autowired
    private OpenAIService openAIService;

    public QnAService(GoogleDriveService googleDriveService, DocumentParserService documentParserService) {
        this.googleDriveService = googleDriveService;
        this.documentParserService = documentParserService;
    }

    public String answerQuestion(String question) {
        StringBuilder allText = new StringBuilder();
        try {
            List<File> files = googleDriveService.listFilesInFolder();
            for (File file : files) {
                try (java.io.InputStream is = googleDriveService.downloadFile(file.getId())) {
                    String text = documentParserService.parseDocument(is, file.getName());
                    allText.append(text).append("\n");
                } catch (Exception e) {
                    // log and skip file
                }
            }
        } catch (Exception e) {
            return "Error accessing knowledge base.";
        }
        // Simple keyword search for relevant chunk
        String[] lines = allText.toString().split("\\r?\\n");
        StringBuilder relevantText = new StringBuilder();
        for (String line : lines) {
            if (line.toLowerCase().contains(question.toLowerCase())) {
                relevantText.append(line).append("\n");
            }
        }
        if (relevantText.length() > 0) {
            // Summarize the relevant text using OpenAI
            return openAIService.summarizeText(relevantText.toString(), question);
        }
        return "Sorry, I couldn't find an answer to your question.";
    }
} 