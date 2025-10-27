package org.duckdns.ahamike.rollbook.config.security;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.config.security.endpoint.RepositoryEndpoint;
import org.duckdns.ahamike.rollbook.config.security.role.RepositoryRole;
import org.duckdns.ahamike.rollbook.config.security.user.RepositoryUser;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.EntityEndpoint;
import org.duckdns.ahamike.rollbook.table.EntityRole;
import org.duckdns.ahamike.rollbook.table.EntityUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceSecurity {
    private final RepositoryUser repositoryUser;
    private final RepositoryEndpoint repositoryEndpoint;
    private final RepositoryRole repositoryRole;

    public GlobalResponse<EntityEndpoint> addRoleToEndpoint(Long endpointId, Long roleId) {
        EntityEndpoint endpoint = repositoryEndpoint.findById(endpointId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Endpoint does not exist"));
        endpoint.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .ifPresent(r -> new ExceptionBusiness(ReturnCode.DATA_ALREADY_ASSIGNED, "Role already assigned to endpoint"));
        EntityRole role = repositoryRole.findById(roleId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        endpoint.addRole(role);

        EntityEndpoint response = repositoryEndpoint.save(endpoint);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityEndpoint> removeRoleFromEndpoint(Long endpointId, Long roleId) {
        EntityEndpoint endpoint = repositoryEndpoint.findById(endpointId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Endpoint does not exist"));
        endpoint.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.DATA_NOT_ASSIGNED, "Role not assigned to endpoint"));
        EntityRole role = repositoryRole.findById(roleId)
            .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        endpoint.removeRole(role);

        EntityEndpoint response = repositoryEndpoint.save(endpoint);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityUser> addRoleToUser(Long userId, Long roleId) {
        EntityUser user = repositoryUser.findById(userId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .ifPresent(r -> new ExceptionBusiness(ReturnCode.DATA_ALREADY_ASSIGNED, "Role already assigned to user"));
        EntityRole role = repositoryRole.findById(roleId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        user.addRole(role);

        EntityUser response = repositoryUser.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityUser> removeRoleFromUser(Long userId, Long roleId) {
        EntityUser user = repositoryUser.findById(userId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.DATA_NOT_ASSIGNED, "Role not assigned to user"));
        EntityRole role = repositoryRole.findById(roleId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        user.removeRole(role);

        EntityUser response = repositoryUser.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }
}
