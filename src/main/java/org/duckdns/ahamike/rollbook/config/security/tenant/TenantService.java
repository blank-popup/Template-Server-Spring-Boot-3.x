package org.duckdns.ahamike.rollbook.config.security.tenant;

import java.util.List;

import org.duckdns.ahamike.rollbook.config.response.BusinessException;
import org.duckdns.ahamike.rollbook.config.response.GlobalResponse;
import org.duckdns.ahamike.rollbook.config.response.ReturnCode;
import org.duckdns.ahamike.rollbook.config.security.tenant.setting.TenantContext;
import org.duckdns.ahamike.rollbook.table.TenantEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    public GlobalResponse<TenantDomain> registerTenant(TenantDomain request) {
        if (TenantContext.isBelongContained(request.getBelong()) == false) {
            throw new BusinessException(ReturnCode.NOT_AVAILABLE_VALUE, "Belong is not available");
        }
        if (tenantRepository.existsByBelongAndName(request.getBelong(), request.getName()) == true) {
            throw new BusinessException(ReturnCode.DATA_ALREADY_EXISTS, "Tenant already exists");
        }

        TenantEntity requestEntity = tenantMapper.toEntity(request);
        TenantEntity responseEntity = tenantRepository.save(requestEntity);
        TenantDomain response = tenantMapper.toDomain(responseEntity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<List<TenantDomain>> getList() {
        List<TenantEntity> responseEntity = tenantRepository.findByIsEnabled(true);
        List<TenantDomain> response = tenantMapper.toDomainList(responseEntity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<TenantDomain> removeTenant(Long tenantId) {
        TenantEntity entity = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Tenant does not exist"));

        entity.setIsEnabled(false);
        TenantEntity responseEntity = tenantRepository.save(entity);
        TenantDomain response = tenantMapper.toDomain(responseEntity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }
}
