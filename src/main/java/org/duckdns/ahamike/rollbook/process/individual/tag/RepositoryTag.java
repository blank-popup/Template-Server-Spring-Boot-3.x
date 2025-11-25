package org.duckdns.ahamike.rollbook.process.individual.tag;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.EntityTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTag extends JpaRepository<EntityTag, Long> {
    List<EntityTag> findByName(String tag);
}
