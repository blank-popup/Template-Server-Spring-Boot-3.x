package org.duckdns.ahamike.rollbook.process.project.terminal;

import org.duckdns.ahamike.rollbook.config.constant.ReturnCode;
import org.duckdns.ahamike.rollbook.config.exception.BusinessException;
import org.duckdns.ahamike.rollbook.process.GlobalResponse;
import org.duckdns.ahamike.rollbook.table.TerminalEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class TerminalService {
    private final TerminalRepository terminalRepository;

    public GlobalResponse<TerminalEntity> registerTerminal(TerminalDomain request) {
        if (terminalRepository.existsByName(request.getName()) == true) {
            throw new BusinessException(ReturnCode.DATA_ALREADY_EXISTS, "Terminal already exists");
        }

        TerminalEntity entity = new TerminalEntity(
                request.getName(),
                request.getDescription()    
        );
        TerminalEntity response = terminalRepository.save(entity);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                response
        );
    }

    public GlobalResponse<TerminalEntity> removeTerminal(Long terminalId) {
        terminalRepository.findById(terminalId)
                .orElseThrow(() -> new BusinessException(ReturnCode.NO_SUCH_DATA, "Terminal does not exist"));

        terminalRepository.deleteById(terminalId);

        ReturnCode code = ReturnCode.OK;
        return new GlobalResponse<>(
                code.getCode(),
                code.getMessage(),
                code.getHttpStatus(),
                null
        );
    }
}
