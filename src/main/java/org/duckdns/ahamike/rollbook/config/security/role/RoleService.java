package org.duckdns.ahamike.rollbook.config.security.role;

import org.duckdns.ahamike.rollbook.config.response.BusinessException;
import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.response.ReturnCode;
import org.duckdns.ahamike.rollbook.table.RoleEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public GlobalResponse<RoleDomain> registerRole(RoleDomain request) {
        if (roleRepository.existsByName(request.getName()) == true) {
            throw new BusinessException(ReturnCode.DATA_ALREADY_EXISTS, "Role already exists");
        }

        RoleEntity requestEntity = roleMapper.toEntity(request);
        RoleEntity responseEntity = roleRepository.save(requestEntity);
        RoleDomain response = roleMapper.toDomain(responseEntity);

        ReturnCode code = ReturnCode.OK;

        return new GlobalResponse<>(code, response);
    }

    public GlobalResponse<RoleDomain> removeRole(Long roleId) {
        RoleEntity entity = roleRepository.findById(roleId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Role does not exist"));

        entity.setIsEnabled(false);
        RoleEntity responseEntity = roleRepository.save(entity);
        RoleDomain response = roleMapper.toDomain(responseEntity);

        ReturnCode code = ReturnCode.OK;

        return new GlobalResponse<>(code, response);
    }
}
