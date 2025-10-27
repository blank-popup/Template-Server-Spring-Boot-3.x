package org.duckdns.ahamike.rollbook.table;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableC;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(
    name = "tb_role",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UNIQUE_NAME",
            columnNames = {
                "name"
            }
        )
    }
)
@AllArgsConstructor
@NoArgsConstructor
public class EntityRole extends AuditableC {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Long active;

    @PrePersist
    public void prePersist() {
        if (this.active == null) {
            this.active = 100L;
        }
    }

    public EntityRole(String name) {
        this.name = name;
    }
}
