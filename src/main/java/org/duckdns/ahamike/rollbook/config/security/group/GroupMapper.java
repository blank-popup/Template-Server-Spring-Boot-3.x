package org.duckdns.ahamike.rollbook.config.security.group;

import java.util.List;

import org.duckdns.ahamike.rollbook.table.GroupEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "id", source = "id")
    GroupDomain toDomain(GroupEntity entity);

    @InheritInverseConfiguration
    GroupEntity toEntity(GroupDomain domain);

    List<GroupDomain> toDomainList(List<GroupEntity> entityList);

    List<GroupEntity> toEntityList(List<GroupDomain> domainList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDomain(GroupDomain domain, @MappingTarget GroupEntity entity);
}
