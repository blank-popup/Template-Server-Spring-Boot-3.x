package org.duckdns.ahamike.rollbook.process.attendance;

import java.time.LocalDateTime;
import java.util.List;

import org.duckdns.ahamike.rollbook.table.EntityAttender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositoryAttendance extends JpaRepository<EntityAttender, Long> {
    @Query(
        value = """
            WITH attendance_ranked AS (
                SELECT
                    a.id                    AS id,
                    a.id_user               AS id_user,
                    t.name                  AS terminal,
                    DATE(a.created_at)      AS date,
                    a.created_at            AS created_at,
                    ROW_NUMBER() OVER (
                        PARTITION BY a.id_user, DATE(a.created_at)
                        ORDER BY a.created_at ASC
                    )                       AS rn_first,
                    ROW_NUMBER() OVER (
                        PARTITION BY a.id_user, DATE(a.created_at)
                        ORDER BY a.created_at DESC
                    )                       AS rn_last
                FROM tb_attender a
                JOIN tb_terminal t
                    ON a.id_terminal = t.id
                WHERE (a.created_at BETWEEN :start AND :end)
                AND (:idUser IS NULL OR a.id_user = :idUser)
            ),
            attendance_first AS (
                SELECT
                    id,
                    id_user,
                    terminal,
                    date,
                    created_at AS created_at_first
                FROM attendance_ranked
                WHERE rn_first = 1
            ),
            attendance_last AS (
                SELECT
                    id,
                    id_user,
                    terminal,
                    date,
                    created_at AS created_at_last
                FROM attendance_ranked
                WHERE rn_last = 1
            )
            SELECT
                f.date                      AS date,
                f.id_user                   AS idUser,
                u.username                  AS username,
                u.name                      AS name,
                tag.name                    AS tag,
                f.id                        AS idFirst,
                f.created_at_first          AS createdAtFirst,
                f.terminal                  AS terminalFirst,
                l.id                        AS idLast,
                l.created_at_last           AS createdAtLast,
                l.terminal                  AS terminalLast
            FROM attendance_first f
            JOIN attendance_last l
                ON f.id_user = l.id_user
                AND f.date = l.date
            JOIN tb_user u
                ON f.id_user = u.id
            LEFT JOIN tb_user_tag ut
                ON u.id = ut.id_user
            LEFT JOIN tb_tag tag
                ON ut.id_tag = tag.id
            ORDER BY f.date, f.id_user
        """,
        nativeQuery = true
    )
    List<ResponseGetAttender> findDailyTagAttendance(@Param("idUser") Long idUser, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
