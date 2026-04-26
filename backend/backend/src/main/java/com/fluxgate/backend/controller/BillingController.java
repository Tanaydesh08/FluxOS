package com.fluxgate.backend.controller;

import com.fluxgate.backend.entity.Plan;
import com.fluxgate.backend.entity.User;
import com.fluxgate.backend.repository.PlanRepository;
import com.fluxgate.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
@RequiredArgsConstructor
public class BillingController {
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @GetMapping("/my-plan")
    public Plan myPlan(){
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow();
        return user.getPlan();
    }

    @PostMapping("/upgrade/{planName}")
    public String upgradePlan(@PathVariable String planName){
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow();
        Plan plan = planRepository.findByName(planName.toUpperCase()).orElseThrow();

        user.setPlan(plan);
        userRepository.save(user);

        return "Plan upgraded to " + plan.getName();
    }
}
