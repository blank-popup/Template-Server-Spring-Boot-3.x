package org.duckdns.ahamike.rollbook.process.agent.menu;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.duckdns.ahamike.rollbook.config.security.role.RoleRepository;
import org.duckdns.ahamike.rollbook.table.MenuEntity;
import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MenuMapper {

    @Autowired
    protected MenuRepository repositoryMenu;

    @Autowired
    protected RoleRepository repositoryRole;

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "roles", expression = "java(toRoleNames(entity.getRoles()))")
    public abstract MenuDomain toDomain(MenuEntity entity);

    @Mapping(target = "parent", expression = "java(toParent(domain.getParentId()))")
    @Mapping(target = "roles", expression = "java(toEntityRoles(domain.getRoles()))")
    public abstract MenuEntity toEntity(MenuDomain domain);

    protected MenuEntity toParent(Long parentId) {
        if (parentId == null) {
            return null;
        }

        return repositoryMenu.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent menu not found: " + parentId));
    }

    protected Set<String> toRoleNames(Set<RoleEntity> roles) {
        if (roles == null) {
            return new HashSet<>();
        }

        return roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }

    protected Set<RoleEntity> toEntityRoles(Set<String> roleNames) {
        if (roleNames == null) {
            return new HashSet<>();
        }

        return roleNames.stream()
                .map(roleName -> repositoryRole.findByName(roleName)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Role not found: " + roleName)
                        )
                )
                .collect(Collectors.toSet());
    }

    @Mapping(target = "parent", expression = "java(toParent(domain.getParentId()))")
    @Mapping(target = "roles", expression = "java(toEntityRoles(domain.getRoles()))")
    public abstract void updateEntityFromDomain(
            MenuDomain domain,
            @MappingTarget MenuEntity entity
    );
}
