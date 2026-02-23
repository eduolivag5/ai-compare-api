package com.example.aimodelsmiddleware.controller;

import com.example.aimodelsmiddleware.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/gemini") // La ruta final será /api/v1/gemini/generate
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Google Gemini", description = "Endpoints para interactuar con Gemini 2.5 Flash")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @Operation(summary = "Generar respuesta", description = "Envía un prompt a Gemini y devuelve el texto procesado.")
    @PostMapping("/generate")
    public String askGemini(@RequestBody Map<String, String> payload) {
        return geminiService.getResponse(payload.get("message"));
    }
}