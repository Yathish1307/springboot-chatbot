package com.university.bot.chatbot.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class DocumentParserService {
    private final Tika tika = new Tika();

    public String parseDocument(InputStream inputStream, String fileName) throws IOException, TikaException {
        // Tika auto-detects file type
        return tika.parseToString(inputStream);
    }
} 