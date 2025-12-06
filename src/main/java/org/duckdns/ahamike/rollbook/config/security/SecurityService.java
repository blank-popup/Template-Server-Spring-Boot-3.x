package org.duckdns.ahamike.rollbook.config.security;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.BusinessException;
import org.duckdns.ahamike.rollbook.config.security.endpoint.EndpointRepository;
import org.duckdns.ahamike.rollbook.config.security.group.GroupRepository;
import org.duckdns.ahamike.rollbook.config.security.role.RoleRepository;
import org.duckdns.ahamike.rollbook.config.security.user.UserRepository;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.EndpointEntity;
import org.duckdns.ahamike.rollbook.table.GroupEntity;
import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.duckdns.ahamike.rollbook.table.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class SecurityService {
    private final UserRepository userRepository;
    private final EndpointRepository endpointRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;

    public GlobalResponse<EndpointEntity> addRoleToEndpoint(Long endpointId, Long roleId) {
        EndpointEntity endpoint = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Endpoint does not exist"));
        endpoint.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .ifPresent(r -> new BusinessException(ReturnCode.DATA_ALREADY_ASSIGNED, "Role already assigned to endpoint"));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        endpoint.addRole(role);

        EndpointEntity response = endpointRepository.save(endpoint);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EndpointEntity> removeRoleFromEndpoint(Long endpointId, Long roleId) {
        EndpointEntity endpoint = endpointRepository.findById(endpointId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Endpoint does not exist"));
        endpoint.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .orElseThrow(() -> new BusinessException(ReturnCode.DATA_NOT_ASSIGNED, "Role not assigned to endpoint"));
        RoleEntity role = roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        endpoint.removeRole(role);

        EndpointEntity response = endpointRepository.save(endpoint);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<UserEntity> addRoleToUser(Long userId, Long roleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .ifPresent(r -> new BusinessException(ReturnCode.DATA_ALREADY_ASSIGNED, "Role already assigned to user"));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        user.addRole(role);

        UserEntity response = userRepository.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<UserEntity> removeRoleFromUser(Long userId, Long roleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .orElseThrow(() -> new BusinessException(ReturnCode.DATA_NOT_ASSIGNED, "Role not assigned to user"));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        user.removeRole(role);

        UserEntity response = userRepository.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<UserEntity> addGroupToUser(Long userId, Long groupId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getGroups().stream().filter(r -> r.getId() == groupId).findAny()
                .ifPresent(r -> new BusinessException(ReturnCode.DATA_ALREADY_ASSIGNED, "Group already assigned to user"));
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Group does not exist"));

        user.addGroup(group);

        UserEntity response = userRepository.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<UserEntity> removeGroupFromUser(Long userId, Long groupId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "User does not exist"));
        user.getGroups().stream().filter(r -> r.getId() == groupId).findAny()
                .orElseThrow(() -> new BusinessException(ReturnCode.DATA_NOT_ASSIGNED, "Group not assigned to user"));
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Group does not exist"));

        user.removeGroup(group);

        UserEntity response = userRepository.save(user);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<GroupEntity> addRoleToGroup(Long groupId, Long roleId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Group does not exist"));
        group.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .ifPresent(r -> new BusinessException(ReturnCode.DATA_ALREADY_ASSIGNED, "Role already assigned to group"));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        group.addRole(role);

        GroupEntity response = groupRepository.save(group);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<GroupEntity> removeRoleFromGroup(Long groupId, Long roleId) {
        GroupEntity group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Group does not exist"));
        group.getRoles().stream().filter(r -> r.getId() == roleId).findAny()
                .orElseThrow(() -> new BusinessException(ReturnCode.DATA_NOT_ASSIGNED, "Role not assigned to group"));
        RoleEntity role = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        group.removeRole(role);

        GroupEntity response = groupRepository.save(group);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }
}
