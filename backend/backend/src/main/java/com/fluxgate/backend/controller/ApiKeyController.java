package com.fluxgate.backend.controller;

import com.fluxgate.backend.entity.ApiKey;
import com.fluxgate.backend.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {
    private final ApiKeyService apiKeyService;

    @PostMapping("/generate")
    public ApiKey generateKey(){
        return apiKeyService.generateApiKey();
    }
}
