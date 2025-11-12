package org.duckdns.ahamike.rollbook.table;

import java.util.Set;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableCU;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    name = "tb_endpoint",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UNIQUE_ENDPOINT",
            columnNames = {
                "name"
            }
        )
    }
)
@AllArgsConstructor
@NoArgsConstructor
public class EntityEndpoint extends AuditableCU {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "method")
    private String method;

    @Column(name = "path")
    private String path;

    @Column(name = "parameter")
    private String parameter;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_endpoint_role",
            joinColumns = @JoinColumn(name = "endpoint_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<EntityRole> roles;

    public EntityEndpoint(String name, String method, String path, String parameter) {
        this.name = name;
        this.method = method;
        this.path = path;
        this.parameter = parameter;
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
