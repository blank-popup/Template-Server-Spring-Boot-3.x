package org.duckdns.ahamike.rollbook.process.project.terminal;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.ExceptionBusiness;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.EntityTerminal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class ServiceTerminal {
    private final RepositoryTerminal repositoryTerminal;

    public GlobalResponse<EntityTerminal> registerTerminal(RequestRegisterTerminal request) {
        if (repositoryTerminal.existsByName(request.getName()) == true) {
            throw new ExceptionBusiness(ReturnCode.DATA_ALREADY_EXISTS, "Terminal already exists");
        }

        EntityTerminal entity = new EntityTerminal(
                request.getName(),
                request.getDescription()    
        );
        EntityTerminal response = repositoryTerminal.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<EntityTerminal> removeTerminal(Long terminalId) {
        repositoryTerminal.findById(terminalId)
                .orElseThrow(() -> new ExceptionBusiness(ReturnCode.NO_SUCH_DATA, "Terminal does not exist"));

        repositoryTerminal.deleteById(terminalId);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                null
        );
    }
}
