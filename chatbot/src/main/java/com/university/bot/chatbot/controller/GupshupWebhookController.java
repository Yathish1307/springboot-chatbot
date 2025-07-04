package com.university.bot.chatbot.controller;

import com.university.bot.chatbot.service.QnAService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@RestController
@RequestMapping("/gupshup/webhook")
public class GupshupWebhookController {
    private static final Logger logger = LoggerFactory.getLogger(GupshupWebhookController.class);

    @Autowired
    private QnAService qnaService;

    @PostMapping
    public String receiveMessage(@RequestBody Map<String, Object> payload) {
        logger.info("Received message from Gupshup: {}", payload);

        String question = extractMessage(payload);
        String phone = extractPhone(payload);

        String answer = qnaService.answerQuestion(question);

        sendReplyToGupshup(phone, answer);

        return "OK";
    }

    private String extractMessage(Map<String, Object> payload) {
        // Example extraction, adjust based on your actual Gupshup payload
        try {
            Map<String, Object> message = (Map<String, Object>) ((Map<String, Object>) payload.get("payload")).get("payload");
            return message.get("text").toString();
        } catch (Exception e) {
            return "";
        }
    }

    private String extractPhone(Map<String, Object> payload) {
        // Example extraction, adjust based on your actual Gupshup payload
        try {
            Map<String, Object> sender = (Map<String, Object>) ((Map<String, Object>) payload.get("payload")).get("sender");
            return sender.get("phone").toString();
        } catch (Exception e) {
            return "";
        }
    }

    private void sendReplyToGupshup(String phone, String answer) {
        String apiUrl = "https://api.gupshup.io/sm/api/v1/msg";
        String apiKey = "t97cy4nkf4juwyayhz8mzq6zkoeksepk";
        String source = "+917834811114";
        String appName = "GenieAI";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", apiKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "channel=whatsapp&source=" + source +
                "&destination=" + phone +
                "&message=" + answer +
                "&src.name=" + appName;

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(apiUrl, entity, String.class);
    }
} 