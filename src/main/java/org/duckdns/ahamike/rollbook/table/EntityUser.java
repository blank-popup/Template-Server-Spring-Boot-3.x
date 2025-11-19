package org.duckdns.ahamike.rollbook.table;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.duckdns.ahamike.rollbook.config.autitable.AuditableCU;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(
    name = "tb_user",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UNIQUE_USERNAME",
            columnNames = {
                "username"
            }
        )
    }
)
@AllArgsConstructor
@NoArgsConstructor
public class EntityUser extends AuditableCU {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private EntityTag tag;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<EntityRole> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_user_group",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_group")
    )
    private Set<EntityGroup> groups = new HashSet<>();

    @Transient
    private boolean passwordChanged = false;

    @Column(name = "passworded_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime passwordedAt;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (passwordChanged == true) {
            this.passwordedAt = LocalDateTime.now();
            this.passwordChanged = false;
        }
    }

    public EntityUser(String username, String password, EntityTag entityTag, String name, String email, String phone, Set<EntityRole> roles) {
        this.username = username;
        this.password = password;
        this.tag = entityTag;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordChanged = true;
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

    public void addGroup(EntityGroup group) {
        if (this.groups.stream().anyMatch(g -> g.getName() != null && g.getName().equals(group.getName())) == false) {
            this.groups.add(group);
        }
    }

    public void removeGroup(EntityGroup group) {
        if (this.groups.stream().anyMatch(g -> g.getName() != null && g.getName().equals(group.getName())) == true) {
            this.groups.remove(group);
        }
    }
}
