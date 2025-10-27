package org.duckdns.ahamike.rollbook.config.security.endpoint;

import java.util.List;
import java.util.Optional;

import org.duckdns.ahamike.rollbook.table.EntityEndpoint;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryEndpoint extends JpaRepository<EntityEndpoint, Long> {
    Optional<EntityEndpoint> findByName(String name);
    @EntityGraph(attributePaths = "roles")
    List<EntityEndpoint> findAll();
}
