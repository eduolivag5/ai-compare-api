package com.example.aimodelsmiddleware.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;

import java.util.List;
import java.util.Map;

@Service
public class Gemma4Service {

    @Value("${gemma.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    private final String GEMMA_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemma-4-31b-it:generateContent?key=";

    public Gemma4Service(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        // Esto confirmará que la key se carga (sin comillas) al arrancar
        System.out.println("Gemma4Service cargado correctamente.");
    }

    public String getResponse(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Instrucción de sistema para evitar que el modelo muestre su razonamiento interno
        String systemInstruction = "Actúa como un asistente de chat directo. " +
                "Responde únicamente con el texto de la respuesta final en el mismo idioma del usuario. " +
                "PROHIBIDO incluir etiquetas como 'Input:', 'Intent:', 'Análisis:' o listas de opciones de respuesta. " +
                "Ve directo al grano.";

        // Construimos el mensaje final combinando la instrucción y la pregunta
        String finalPrompt = systemInstruction + "\n\nPregunta del usuario: " + prompt;

        // IMPORTANTE: Aquí usamos finalPrompt en lugar de prompt
        Map<String, Object> part = Map.of("text", finalPrompt);
        Map<String, Object> content = Map.of("parts", List.of(part));

        Map<String, Object> generationConfig = Map.of(
                "temperature", 0.7, // Mantiene la respuesta creativa pero controlada
                "topP", 0.95,
                "maxOutputTokens", 4096
        );

        // El cuerpo de la petición con los contenidos y la configuración
        Map<String, Object> body = Map.of(
                "contents", List.of(content),
                "generationConfig", generationConfig
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // Enviamos la petición al endpoint de Google (Gemma 4)
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(GEMMA_URL + apiKey, request, Map.class);
            return extractTextFromResponse(response);
        } catch (Exception e) {
            return "Error al contactar con Gemma 4: " + e.getMessage();
        }
    }

    private String extractTextFromResponse(Map<String, Object> response) {
        try {
            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);
            return (String) firstPart.get("text");
        } catch (Exception e) {
            return "Error parseando respuesta: " + response.toString();
        }
    }
}