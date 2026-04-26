package com.fluxgate.backend.config;

import com.fluxgate.backend.entity.Plan;
import com.fluxgate.backend.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final PlanRepository planRepository;

    @Override
    public void run(String... args){
        if (planRepository.count() == 0){
            planRepository.save(
                    Plan.builder()
                            .name("FREE")
                            .requestPerMinute(5)
                            .monthlyQuota(10000)
                            .price(0.0)
                            .build()
            );

            planRepository.save(
                    Plan.builder()
                            .name("PRO")
                            .requestPerMinute(100)
                            .monthlyQuota(500000)
                            .price(999.0)
                            .build()
            );
        }
    }
}
