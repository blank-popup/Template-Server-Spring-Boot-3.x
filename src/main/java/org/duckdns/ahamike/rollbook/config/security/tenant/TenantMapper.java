package org.duckdns.ahamike.rollbook.config.security.tenant;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.TenantEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TenantMapper {

    @Mapping(target = "id", source = "id")
    TenantDomain toDomain(TenantEntity entity);

    @InheritInverseConfiguration
    TenantEntity toEntity(TenantDomain domain);

    List<TenantDomain> toDomainList(List<TenantEntity> entityList);

    List<TenantEntity> toEntityList(List<TenantDomain> domainList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDomain(TenantDomain domain, @MappingTarget TenantEntity entity);
}
