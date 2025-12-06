package org.duckdns.ahamike.rollbook.table;

import java.util.Set;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableCU;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
    name = "tb_menu",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UNIQUE_NAME",
            columnNames = {
                "parent_id",
                "name"
            }
        )
    }
)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MenuEntity extends AuditableCU {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MenuEntity parent;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "ordering")
    private Integer ordering;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_menu_role",
            joinColumns = @JoinColumn(name = "menu_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;
}
