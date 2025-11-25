package org.duckdns.ahamike.rollbook.table;

import java.util.HashSet;
import java.util.Set;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableCU;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    name = "tb_group",
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
public class EntityGroup extends AuditableCU {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_group_role",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<EntityRole> roles = new HashSet<>();

    public EntityGroup(String name) {
        this.name = name;
    }

    public void addRole(EntityRole role) {
        if (this.roles.stream().anyMatch(r -> r.getName() != null && r.getName().equals(role.getName())) == false) {
            this.roles.add(role);
        }
    }

    public void removeRole(EntityRole role) {
        if (this.roles.stream().anyMatch(r -> r.getName() != null && r.getName().equals(role.getName())) == true) {
            this.roles.remove(role);
        }
    }
}
