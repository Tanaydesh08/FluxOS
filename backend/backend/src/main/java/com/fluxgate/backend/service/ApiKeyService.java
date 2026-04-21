package com.fluxgate.backend.service;

import com.fluxgate.backend.entity.ApiKey;
import com.fluxgate.backend.entity.User;
import com.fluxgate.backend.repository.ApiKeyRepository;
import com.fluxgate.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;
    private final UserRepository userRepository;

    public ApiKey generateApiKey(){
        String email = (String) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found...!!!"));
        String key = UUID.randomUUID().toString();

        ApiKey apiKey = ApiKey.builder()
                .key(key)
                .user(user)
                .build();
        return apiKeyRepository.save(apiKey);
    }
}
