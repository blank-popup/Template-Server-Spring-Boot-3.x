package org.duckdns.ahamike.rollbook.config.security.endpoint;

import java.util.List;
import java.util.Optional;

import org.duckdns.ahamike.rollbook.table.EndpointEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndpointRepository extends JpaRepository<EndpointEntity, Long> {
    Optional<EndpointEntity> findByName(String name);
    @EntityGraph(attributePaths = "roles")
    List<EndpointEntity> findAll();
}
