package org.duckdns.ahamike.rollbook.process.file;

import org.duckdns.ahamike.rollbook.table.EntityTransferFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryTransferFile extends JpaRepository<EntityTransferFile, Long> {
  
}
