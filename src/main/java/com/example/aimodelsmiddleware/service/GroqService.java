package com.example.aimodelsmiddleware.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.List;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    // URL oficial de Groq (compatible con OpenAI)
    private final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    public GroqService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // Llama 3.3 70b es un modelo excelente para Groq
        Map<String, Object> message = Map.of("role", "user", "content", prompt);
        Map<String, Object> body = Map.of(
                "model", "llama-3.3-70b-versatile",
                "messages", List.of(message)
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            Map<String, Object> response = restTemplate.postForObject(GROQ_URL, request, Map.class);
            return extractTextFromGroq(response);
        } catch (Exception e) {
            return "Error al contactar con Groq (Llama): " + e.getMessage();
        }
    }

    private String extractTextFromGroq(Map<String, Object> response) {
        try {
            List choices = (List) response.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Error parseando respuesta de Groq: " + e.getMessage();
        }
    }
}