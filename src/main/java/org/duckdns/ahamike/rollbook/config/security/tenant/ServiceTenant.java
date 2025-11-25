package org.duckdns.ahamike.rollbook.config.security.tenant;

import java.util.List;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.config.security.tenant.setting.TenantContext;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.EntityTenant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceTenant {

    private final RepositoryTenant repositoryTenant;

    public GlobalResponse<EntityTenant> registerTenant(RequestRegisterTenant request) {
        if (TenantContext.isBelongContained(request.getBelong()) == false) {
            throw new ExceptionBusiness(ReturnCode.NOT_AVAILABLE_VALUE, "Belong is not available");
        }
        if (repositoryTenant.existsByBelongAndName(request.getBelong(), request.getName()) == true) {
            throw new ExceptionBusiness(ReturnCode.DATA_ALREADY_EXISTS, "Tenant already exists");
        }

        EntityTenant entity = new EntityTenant(
                request.getBelong(),
                request.getName(),
                request.getDescription()
        );
        EntityTenant response = repositoryTenant.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<List<EntityTenant>> getListTenant() {
        List<EntityTenant> response = repositoryTenant.findByIsEnabled(true);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityTenant> removeTenant(Long tenantId) {
        EntityTenant entity = repositoryTenant.findById(tenantId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Tenant does not exist"));

        entity.setIsEnabled(false);
        EntityTenant response = repositoryTenant.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }
}
