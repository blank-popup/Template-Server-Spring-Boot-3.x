package org.duckdns.ahamike.rollbook.process.individual.attendance;

import java.time.LocalDateTime;
import java.util.List;

import org.duckdns.ahamike.rollbook.table.EntityAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositoryAttendance extends JpaRepository<EntityAttendance, Long> {
    @Query(
        value = """
            WITH ranked_attendance AS (
                SELECT
                    a.id                    AS id,
                    a.user_id               AS user_id,
                    t.name                  AS terminal,
                    DATE(a.created_at)      AS date,
                    a.created_at            AS created_at,
                    ROW_NUMBER() OVER (
                        PARTITION BY a.user_id, DATE(a.created_at)
                        ORDER BY a.created_at ASC
                    )                       AS first_rn,
                    ROW_NUMBER() OVER (
                        PARTITION BY a.user_id, DATE(a.created_at)
                        ORDER BY a.created_at DESC
                    )                       AS last_rn
                FROM tb_attendance a
                JOIN tb_terminal t
                    ON a.terminal_id = t.id
                WHERE (a.created_at BETWEEN :start AND :end)
                AND (:userId IS NULL OR a.user_id = :userId)
            ),
            first_attendance AS (
                SELECT
                    id,
                    user_id,
                    terminal,
                    date,
                    created_at AS first_created_at
                FROM ranked_attendance
                WHERE first_rn = 1
            ),
            last_attendance AS (
                SELECT
                    id,
                    user_id,
                    terminal,
                    date,
                    created_at AS last_created_at
                FROM ranked_attendance
                WHERE last_rn = 1
            )
            SELECT
                f.date                      AS date,
                f.user_id                   AS userId,
                u.username                  AS username,
                u.name                      AS name,
                tag.name                    AS tag,
                f.id                        AS firstId,
                f.first_created_at          AS firstCreatedAt,
                f.terminal                  AS firstTerminal,
                l.id                        AS lastId,
                l.last_created_at           AS lastCreatedAt,
                l.terminal                  AS lastTerminal
            FROM first_attendance f
            JOIN last_attendance l
                ON f.user_id = l.user_id
                AND f.date = l.date
            JOIN tb_user u
                ON f.user_id = u.id
            LEFT JOIN tb_tag tag
                ON u.tag_id = tag.id
            ORDER BY f.date, f.user_id
        """,
        nativeQuery = true
    )
    List<ResponseGetAttendance> findDailyTagAttendance(@Param("userId") Long userId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
