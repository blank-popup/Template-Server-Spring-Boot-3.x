package org.duckdns.ahamike.rollbook.config.security.group;

import org.duckdns.ahamike.rollbook.table.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<GroupEntity, Long>{
    boolean existsByName(String name);
}
