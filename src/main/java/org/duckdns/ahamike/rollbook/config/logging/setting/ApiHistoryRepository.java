package org.duckdns.ahamike.rollbook.config.logging.setting;

import org.duckdns.ahamike.rollbook.table.ApiHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiHistoryRepository extends JpaRepository<ApiHistoryEntity, Long> {

}
