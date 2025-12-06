package org.duckdns.ahamike.rollbook.config.security.tenant;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.TenantEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(target = "id", source = "id")
    TenantDomain toDomain(TenantEntity entity);

    @InheritInverseConfiguration
    TenantEntity toEntity(TenantDomain domain);

    List<TenantDomain> toDomainList(List<TenantEntity> entityList);

    List<TenantEntity> toEntityList(List<TenantDomain> domainList);
}
