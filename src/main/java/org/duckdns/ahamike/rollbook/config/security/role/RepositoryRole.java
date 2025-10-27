package org.duckdns.ahamike.rollbook.config.security.role;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.EntityRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryRole extends JpaRepository<EntityRole, Long> {
    List<EntityRole> findAllByNameIn(List<String> roles);
    boolean existsByName(String name);
}
