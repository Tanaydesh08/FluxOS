package com.fluxgate.backend.controller;

import com.fluxgate.backend.entity.ApiUsage;
import com.fluxgate.backend.repository.ApiUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usage")
@RequiredArgsConstructor
public class UsageController {
    private final ApiUsageRepository apiUsageRepository;

    @GetMapping
    public List<ApiUsage> getAllUsage(){
        return apiUsageRepository.findAll();
    }

    @GetMapping("/count")
    public long getUsageCount(){
        return apiUsageRepository.count();
    }
}
