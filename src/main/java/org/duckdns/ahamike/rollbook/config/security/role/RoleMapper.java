package org.duckdns.ahamike.rollbook.config.security.role;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", source = "id")
    RoleDomain toDomain(RoleEntity entity);

    @InheritInverseConfiguration
    RoleEntity toEntity(RoleDomain domain);

    List<RoleDomain> toDomainList(List<RoleEntity> entityList);

    List<RoleEntity> toEntityList(List<RoleDomain> domainList);
}
