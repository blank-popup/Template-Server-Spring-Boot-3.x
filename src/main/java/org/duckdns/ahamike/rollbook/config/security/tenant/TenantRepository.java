package org.duckdns.ahamike.rollbook.config.security.tenant;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<TenantEntity, Long> {
    boolean existsByBelongAndName(String belong, String name);
    List<TenantEntity> findByIsEnabled(Boolean isEnabled);
}
