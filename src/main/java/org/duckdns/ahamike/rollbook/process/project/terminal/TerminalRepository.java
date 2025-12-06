package org.duckdns.ahamike.rollbook.process.project.terminal;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.TerminalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<TerminalEntity, Long> {
    List<TerminalEntity> findByName(String terminal);
    boolean existsByName(String name);
}
