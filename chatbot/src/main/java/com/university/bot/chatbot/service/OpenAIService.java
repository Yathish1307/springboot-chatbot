package com.university.bot.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OpenAIService {
    @Value("${openai.api.key}")
    private String openaiApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .build();

    public String summarizeText(String text, String question) {
        String prompt = "Summarize the following content to answer the question: '" + question + "'\n\nContent:\n" + text;
        String requestBody = "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"system\", \"content\": \"You are a helpful assistant.\"},\n" +
                "    {\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}\n" +
                "  ]\n" +
                "}";

        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + openaiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    int idx = response.indexOf("\"content\":\"");
                    if (idx != -1) {
                        int start = idx + 11;
                        int end = response.indexOf("\"", start);
                        if (end != -1) {
                            return response.substring(start, end).replace("\\n", "\n");
                        }
                    }
                    return "Could not summarize the answer.";
                })
                .block();
    }
} 