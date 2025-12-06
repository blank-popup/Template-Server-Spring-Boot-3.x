package org.duckdns.ahamike.rollbook.config.security.role;

import java.util.Optional;
import java.util.Set;

import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Set<RoleEntity> findAllByNameIn(Set<String> roles);
    boolean existsByName(String name);
    Optional<RoleEntity> findByName(String roleName);
}
