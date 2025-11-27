package org.duckdns.ahamike.rollbook.config.security.role;

import java.util.Optional;
import java.util.Set;

import org.duckdns.ahamike.rollbook.table.EntityRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryRole extends JpaRepository<EntityRole, Long> {
    Set<EntityRole> findAllByNameIn(Set<String> roles);
    boolean existsByName(String name);
    Optional<EntityRole> findByName(String roleName);
}
