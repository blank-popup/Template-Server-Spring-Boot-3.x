package org.duckdns.ahamike.rollbook.config.security.group;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.EntityGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceGroup {
    private final RepositoryGroup repositoryGroup;

    public GlobalResponse<EntityGroup> registerGroup(String groupName) {
        if (repositoryGroup.existsByName(groupName) == true) {
            throw new ExceptionBusiness(ReturnCode.DATA_ALREADY_EXISTS, "Group already exists");
        }

        EntityGroup entity = new EntityGroup(groupName);
        EntityGroup response = repositoryGroup.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityGroup> removeGroup(Long groupId) {
        repositoryGroup.findById(groupId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Group does not exist"));

        repositoryGroup.deleteById(groupId);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                null
        );
    }
}
