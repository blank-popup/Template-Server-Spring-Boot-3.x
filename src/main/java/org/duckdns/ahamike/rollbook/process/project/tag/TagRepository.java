package org.duckdns.ahamike.rollbook.process.project.tag;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    List<TagEntity> findByName(String tag);
}
