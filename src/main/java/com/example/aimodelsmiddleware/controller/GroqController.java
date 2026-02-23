package com.example.aimodelsmiddleware.controller;

import com.example.aimodelsmiddleware.service.GroqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/groq") // Ruta final: /api/v1/groq/generate
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Groq (Meta Llama)", description = "Endpoints para interactuar con Llama 3.3 vía Groq")
public class GroqController {

    private final GroqService groqService;

    public GroqController(GroqService groqService) {
        this.groqService = groqService;
    }

    @Operation(summary = "Generar respuesta con Llama via Groq", description = "Aprovecha la baja latencia de Groq para obtener respuestas de modelos Llama.")
    @PostMapping("/generate")
    public String askGroq(@RequestBody Map<String, String> payload) {
        return groqService.getResponse(payload.get("message"));
    }
}