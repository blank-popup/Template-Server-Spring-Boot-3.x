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

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(
    name = "tb_terminal",
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
public class EntityTerminal extends AuditableCU {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    public EntityTerminal(String name) {
        this.name = name;
    }
}
