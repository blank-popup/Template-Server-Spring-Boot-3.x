package org.duckdns.ahamike.rollbook.config.security.user;

import java.util.List;
import java.util.Optional;

import org.duckdns.ahamike.rollbook.table.EntityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RepositoryUser extends JpaRepository<EntityUser, Long> {
    @Query(
        value = """
            SELECT u
            FROM EntityUser u
            JOIN FETCH u.tag t
            JOIN FETCH u.roles r
            WHERE (:username is null OR u.username = :username)
                AND (:name is null OR u.name = :name)
                AND (:tag is null OR t.name = :tag)
        """
    )
    List<EntityUser> findByNotNullUsernameNameTag(@Param("username") String username, @Param("name") String name, @Param("tag") String tag);
    Optional<EntityUser> findByUsername(String username);
    List<EntityUser> findByTag_Name(String tag);
}
