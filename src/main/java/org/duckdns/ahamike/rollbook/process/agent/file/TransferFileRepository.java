package org.duckdns.ahamike.rollbook.process.agent.file;

import org.duckdns.ahamike.rollbook.table.TransferFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferFileRepository extends JpaRepository<TransferFileEntity, Long> {
  
}
