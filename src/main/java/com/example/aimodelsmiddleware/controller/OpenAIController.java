package com.example.aimodelsmiddleware.controller;

import com.example.aimodelsmiddleware.service.OpenAIService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/openai")
@Tag(name = "OpenAI", description = "Endpoints para interactuar con GPT-4o-mini")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @Operation(summary = "Generar respuesta con GPT", description = "Envía un mensaje a los modelos de OpenAI y recibe la respuesta generada.")
    @PostMapping("/generate")
    public String askOpenAI(@RequestBody Map<String, String> payload) {
        return openAIService.getResponse(payload.get("message"));
    }
}