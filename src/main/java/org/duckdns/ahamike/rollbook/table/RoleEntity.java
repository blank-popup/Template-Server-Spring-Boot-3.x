package org.duckdns.ahamike.rollbook.table;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableCU;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
@SuperBuilder
public class RoleEntity extends AuditableCU {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public RoleEntity(String name) {
        super();
        this.name = name;
    }
}
