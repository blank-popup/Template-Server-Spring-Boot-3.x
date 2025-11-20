package org.duckdns.ahamike.rollbook.config.security.tenant;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.EntityTenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTenant extends JpaRepository<EntityTenant, Long> {
    boolean existsByBelongAndName(String belong, String name);
    List<EntityTenant> findByIsEnabled(Boolean isEnabled);
}
