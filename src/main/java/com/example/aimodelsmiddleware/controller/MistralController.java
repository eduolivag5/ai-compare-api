package com.example.aimodelsmiddleware.controller;

import com.example.aimodelsmiddleware.service.MistralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/mistral")
@Tag(name = "Mistral AI", description = "Endpoints para interactuar con Mistral Large")
public class MistralController {

    private final MistralService mistralService;

    public MistralController(MistralService mistralService) {
        this.mistralService = mistralService;
    }

    @Operation(summary = "Generar respuesta con Mistral", description = "Envía un mensaje a la API de Mistral AI.")
    @PostMapping("/generate")
    public String askMistral(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        return mistralService.getResponse(message);
    }
}