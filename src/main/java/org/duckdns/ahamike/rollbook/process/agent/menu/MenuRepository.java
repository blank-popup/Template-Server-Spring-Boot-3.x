package org.duckdns.ahamike.rollbook.process.agent.menu;

import java.util.List;
import java.util.Set;

import org.duckdns.ahamike.rollbook.table.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {

    @Query("""
        SELECT DISTINCT m FROM MenuEntity m
        LEFT JOIN m.roles r
        WHERE m.isEnabled = true
            AND r.name IN :roleNames
        ORDER BY 
            CASE WHEN m.parent IS NULL THEN m.id ELSE m.parent.id END,
            m.ordering
    """)
    List<MenuEntity> findMenusByRoles(@Param("roleNames") Set<String> roleNames);
}
