package com.example.aimodelsmiddleware.controller;

import com.example.aimodelsmiddleware.service.Gemma4Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/gemma4")
@CrossOrigin(origins = "*") // Permite peticiones desde tu front de Vite
@Tag(name = "Google Gemma 4", description = "Endpoints para Gemma 4 31B")
public class GemmaController {

    private final Gemma4Service gemma4Service;

    public GemmaController(Gemma4Service gemma4Service) {
        this.gemma4Service = gemma4Service;
    }

    @Operation(summary = "Generar respuesta con Gemma 4")
    @PostMapping("/generate")
    public String askGemma(@RequestBody Map<String, String> payload) {
        return gemma4Service.getResponse(payload.get("message"));
    }
}