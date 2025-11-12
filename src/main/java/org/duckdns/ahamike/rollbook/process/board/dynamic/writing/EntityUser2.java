package org.duckdns.ahamike.rollbook.process.board.dynamic.writing;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_user2")
public class EntityUser2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    public EntityUser2() {}

    public EntityUser2(String username) {
        this.username = username;
    }

    // getters/setters
}
