package com.fluxgate.backend.repository;

import com.fluxgate.backend.entity.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {
}
