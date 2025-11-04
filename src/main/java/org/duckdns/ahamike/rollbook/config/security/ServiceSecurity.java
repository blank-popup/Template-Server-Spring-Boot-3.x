package org.duckdns.ahamike.rollbook.config.security;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.config.security.endpoint.RepositoryEndpoint;
import org.duckdns.ahamike.rollbook.config.security.group.RepositoryGroup;
import org.duckdns.ahamike.rollbook.config.security.role.RepositoryRole;
import org.duckdns.ahamike.rollbook.config.security.user.RepositoryUser;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.EntityEndpoint;
import org.duckdns.ahamike.rollbook.table.EntityGroup;
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
    private final RepositoryGroup repositoryGroup;
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

    public GlobalResponse<EntityUser> addGroupToUser(Long userId, Long groupId) {
        EntityUser user = repositoryUser.findById(userId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getGroups().stream().filter(r -> r.getId() == groupId).findAny()
                .ifPresent(r -> new ExceptionBusiness(ReturnCode.DATA_ALREADY_ASSIGNED, "Group already assigned to user"));
        EntityGroup group = repositoryGroup.findById(groupId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Group does not exist"));

        user.addGroup(group);

        EntityUser response = repositoryUser.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityUser> removeGroupFromUser(Long userId, Long groupId) {
        EntityUser user = repositoryUser.findById(userId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getGroups().stream().filter(r -> r.getId() == groupId).findAny()
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.DATA_NOT_ASSIGNED, "Group not assigned to user"));
        EntityGroup group = repositoryGroup.findById(groupId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Group does not exist"));

        user.removeGroup(group);

        EntityUser response = repositoryUser.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityGroup> addRoleToGroup(Long groupId, Long roleId) {
        EntityGroup group = repositoryGroup.findById(groupId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Group does not exist"));
        group.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .ifPresent(r -> new ExceptionBusiness(ReturnCode.DATA_ALREADY_ASSIGNED, "Role already assigned to group"));
        EntityRole role = repositoryRole.findById(roleId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        group.addRole(role);

        EntityGroup response = repositoryGroup.save(group);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityGroup> removeRoleFromGroup(Long groupId, Long roleId) {
        EntityGroup group = repositoryGroup.findById(groupId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Group does not exist"));
        group.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.DATA_NOT_ASSIGNED, "Role not assigned to group"));
        EntityRole role = repositoryRole.findById(roleId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        group.removeRole(role);

        EntityGroup response = repositoryGroup.save(group);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }
}
