package org.duckdns.ahamike.rollbook.config.security.user;

import java.util.List;
import java.util.Optional;

import org.duckdns.ahamike.rollbook.table.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query(
        value = """
            SELECT u
            FROM UserEntity u
            LEFT JOIN FETCH u.tag t
            LEFT JOIN FETCH u.roles r
            LEFT JOIN FETCH u.groups g
            WHERE (:username is null OR u.username = :username)
                AND (:name is null OR u.name = :name)
                AND (:tag is null OR t.name = :tag)
        """
    )
    List<UserEntity> findByNotNullUsernameNameTag(@Param("username") String username, @Param("name") String name, @Param("tag") String tag);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByTag_Name(String tag);
}
