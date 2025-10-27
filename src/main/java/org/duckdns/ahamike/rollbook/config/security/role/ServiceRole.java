package org.duckdns.ahamike.rollbook.config.security.role;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.EntityRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceRole {
    private final RepositoryRole repositoryRole;

    public GlobalResponse<EntityRole> registerRole(String roleName) {
        if (repositoryRole.existsByName(roleName) == true) {
            throw new ExceptionBusiness(ReturnCode.DATA_ALREADY_EXISTS, "Role already exists");
        }

        EntityRole entity = new EntityRole(roleName);
        EntityRole response = repositoryRole.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityRole> removeRole(Long roleId) {
        repositoryRole.findById(roleId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        repositoryRole.deleteById(roleId);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                null
        );
    }
}
