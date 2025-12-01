package org.duckdns.ahamike.rollbook.process.project.terminal;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.EntityTerminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTerminal extends JpaRepository<EntityTerminal, Long> {
    List<EntityTerminal> findByName(String terminal);
    boolean existsByName(String name);
}
