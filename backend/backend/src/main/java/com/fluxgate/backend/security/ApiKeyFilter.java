package com.fluxgate.backend.security;

import com.fluxgate.backend.entity.ApiKey;
import com.fluxgate.backend.entity.ApiUsage;
import com.fluxgate.backend.entity.User;
import com.fluxgate.backend.repository.ApiKeyRepository;
import com.fluxgate.backend.repository.ApiUsageRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {
    private final ApiKeyRepository apiKeyRepository;
    private final StringRedisTemplate redisTemplate;
    private final ApiUsageRepository apiUsageRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        String uri = request.getRequestURI();

        //protected gateway routes only
        if (!uri.startsWith("/gateway")) {
            filterChain.doFilter(request, response);
            return;
        }

        String apiKey = request.getHeader("x-api-key");

        //if Key is Missing
        if (apiKey == null || apiKey.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API Key");
            return;
        }

        // if key is invalid
        var key = apiKeyRepository.findByKey(apiKey);
        if (key.isEmpty()){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid API key");
            return;
        }
        //Get ApiKey Entity
        ApiKey apiKeyEntity = key.get();

        //Get Linked User
        User user = apiKeyEntity.getUser();

        //Get plan-based limit
        int LIMIT = user.getPlan().getRequestPerMinute();

        //Limiting the rate
        String redisKey = "rate_limit: " + apiKey;

        Long count = redisTemplate.opsForValue().increment(redisKey);

        //First Request in the window -> expiry set
        if (count != null && count == 1){
            redisTemplate.expire(redisKey, Duration.ofMinutes(1));
        }
        //Block if over limit
        if (count != null && count > LIMIT) {
            response.setStatus(429);
            response.getWriter().write("Rate limit exceeded for your plan...!!!!");
            return;
        }
        apiUsageRepository.save(
                ApiUsage.builder()
                        .apiKey(apiKey)
                        .endpoint(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
        filterChain.doFilter(request, response);
    }
}
