package org.duckdns.ahamike.rollbook.config.security.group;

import org.duckdns.ahamike.rollbook.table.EntityGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryGroup extends JpaRepository<EntityGroup, Long>{
    boolean existsByName(String name);
}
